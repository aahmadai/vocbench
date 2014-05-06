package org.fao.aims.aos.vocbench.services;

import it.uniroma2.art.owlart.exceptions.ModelAccessException;
import it.uniroma2.art.owlart.exceptions.QueryEvaluationException;
import it.uniroma2.art.owlart.exceptions.UnsupportedQueryLanguageException;
import it.uniroma2.art.owlart.model.ARTLiteral;
import it.uniroma2.art.owlart.models.SKOSXLModel;
import it.uniroma2.art.owlart.query.MalformedQueryException;
import it.uniroma2.art.owlart.query.TupleBindings;
import it.uniroma2.art.owlart.query.TupleBindingsIterator;
import it.uniroma2.art.owlart.query.TupleQuery;
import it.uniroma2.art.semanticturkey.exceptions.HTTPParameterUnspecifiedException;
import it.uniroma2.art.semanticturkey.plugin.extpts.ServiceAdapter;
import it.uniroma2.art.semanticturkey.servlet.Response;
import it.uniroma2.art.semanticturkey.servlet.ServiceVocabulary.RepliesStatus;
import it.uniroma2.art.semanticturkey.servlet.XMLResponseREPLY;
import it.uniroma2.art.semanticturkey.servlet.main.SKOS;
import it.uniroma2.art.semanticturkey.utilities.XMLHelp;

import static org.fao.aims.aos.vocbench.services.VocBenchResourcesURI.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.w3c.dom.Element;

@Component
public class Agrovoc extends ServiceAdapter {

	protected static Logger logger = LoggerFactory.getLogger(Agrovoc.class);

	public static class Req {

		// STATS REQUESTS
		public static final String getStatsAgrovocRequest = "getStatsAgrovoc";

	}

	public static class ParAgrovoc {

	}

	@Autowired
	public Agrovoc(@Value("agrovoc") String id) {
		super(id);
	}

	@Override
	protected Response getPreCheckedResponse(String request) throws HTTPParameterUnspecifiedException {
		logger.debug("request to Vocbench");

		Response response = null;
		if (request == null)
			return servletUtilities.createNoSuchHandlerExceptionResponse(request);

		// Statistics
		if (request.equals(Req.getStatsAgrovocRequest)) {
			String schemeURI = setHttpPar(SKOS.Par.scheme);
			checkRequestParametersAllNotNull(SKOS.Par.scheme);
			response = getStatsAgrovoc(schemeURI);
		} 
		
		else
			return servletUtilities.createNoSuchHandlerExceptionResponse(request);

		this.fireServletEvent();
		return response;
	}

	@Override
	protected Logger getLogger() {
		return null;
	}

	public Response getStatsAgrovoc(String schemeUri) {
		SKOSXLModel skosxlModel = getSKOSXLModel();
		try {
			XMLResponseREPLY response = createReplyResponse(RepliesStatus.ok);
			Element dataElement = response.getDataElement();
			
			String query ;
			// Numbers of available subvocabularies;
			query = "SELECT DISTINCT ?subVoc" + 
					"\nWHERE{" + 
					"\n?subj <" + ISPARTOFSUBVOCABULARY + "> ?subVoc . " + 
					"\n}";
			TupleQuery tupleQuery = skosxlModel.createTupleQuery(query);
			TupleBindingsIterator iter = tupleQuery.evaluate(true);

			List<ARTLiteral> subVocList = new ArrayList<ARTLiteral>();

			TupleBindings tuple;
			while (iter.hasNext()) {
				tuple = iter.next();
				ARTLiteral subVoc = tuple.iterator().next().getBoundValue().asLiteral();
				if (subVoc.getLabel() != null && subVoc.getDatatype() != null)
					subVocList.add(subVoc);
			}
			iter.close();
			Element subVocabulariesElem = XMLHelp.newElement(dataElement, "SubVocabularies");
			subVocabulariesElem.setAttribute("number", subVocList.size() + "");
			for (ARTLiteral subvoc : subVocList) {
				query = "SELECT ?subj " + 
						"\nWHERE{" + 
						"\n?termSubj <" + ISPARTOFSUBVOCABULARY + "> \""+ subvoc.getLabel() + 
							"\"^^<" + subvoc.getDatatype().getURI() + ">" + 
						"\n}";
				tupleQuery = skosxlModel.createTupleQuery(query);
				TupleBindingsIterator tupleIter = tupleQuery.evaluate(true);
				int count = 0;
				while (tupleIter.streamOpen()) {
					++count;
					tupleIter.getNext();
				}
				tupleIter.close();

				Element subVocElem = XMLHelp.newElement(subVocabulariesElem, "SubVocabulary");
				subVocElem.setAttribute("subVocName", subvoc.getLabel());
				subVocElem.setAttribute("occurrences", count + "");
			}

			// Numbers of terms belonging to each subvocabulary, possibly distinguished by language;
			query = "SELECT ?termType ?label" + 
					"\nWHERE{" + 
					"\n?xlabel <" + HASTERMTYPE + "> ?termType ." + 
					"\n?xlabel <" + LITERALFORM + "> ?label ." + 
					"\n}";
			tupleQuery = skosxlModel.createTupleQuery(query);
			TupleBindingsIterator tupleIter = tupleQuery.evaluate(true);
			Map<String, Map<String, Integer>> termTypeLangCountMap = new HashMap<String, Map<String, Integer>>();
			while (tupleIter.hasNext()) {
				tuple = tupleIter.getNext();
				String termType = tuple.getBinding("termType").getBoundValue().asLiteral().getLabel();
				String lang = tuple.getBinding("label").getBoundValue().asLiteral().getLanguage();
				if (!termTypeLangCountMap.containsKey(termType))
					termTypeLangCountMap.put(termType, new HashMap<String, Integer>());
				Map<String, Integer> langCountMap = termTypeLangCountMap.get(termType);
				if (!langCountMap.containsKey(lang))
					langCountMap.put(lang, 1);
				else
					langCountMap.put(lang, langCountMap.get(lang) + 1);
			}
			tupleIter.close();
			Element termSubVocabulariesElem = XMLHelp.newElement(dataElement, "termSubVocabularies");
			termSubVocabulariesElem.setAttribute("number", termTypeLangCountMap.size() + "");
			for (String termType : termTypeLangCountMap.keySet()) {
				Element termSubVocabularyElem = XMLHelp.newElement(termSubVocabulariesElem,
						"termSubVocabulary");
				termSubVocabularyElem.setAttribute("name", termType);
				Map<String, Integer> langCountMap = termTypeLangCountMap.get(termType);
				int count = 0;
				for (String lang : langCountMap.keySet()) {
					Element langElem = XMLHelp.newElement(termSubVocabularyElem, "lang");
					langElem.setAttribute("name", lang);
					count += langCountMap.get(lang);
					langElem.setAttribute("occurrences", langCountMap.get(lang).toString());
				}
				termSubVocabularyElem.setAttribute("occurrences", count + "");
			}

			return response;

		} catch (ModelAccessException e) {
			return logAndSendException(e);
		} catch (QueryEvaluationException e) {
			return logAndSendException(e);
		} catch (UnsupportedQueryLanguageException e) {
			return logAndSendException(e);
		} catch (MalformedQueryException e) {
			return logAndSendException(e);
		}
	}

	//TODO understand how to remove this duplication
	public SKOSXLModel getSKOSXLModel() {
		return (SKOSXLModel)getOntModel();
	}

}
