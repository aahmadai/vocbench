package org.fao.aims.aos.vocbench.services;

public class VocBenchResourcesURI {

	
	final static public String LUCENEIMPORT = "http://www.ontotext.com/owlim/lucene#";
	final static public String LUCENEINDEX = "http://www.ontotext.com/owlim/lucene#vocbench";
	//final static public String LUCENEINDEX = "http://www.ontotext.com/owlim/lucene#agrovoc"; // old name
	
	//final static public String AGROVOCNAMESPACE = "http://aims.fao.org/aos/agrovoc/";
	//final static public String STARTURIFORSTATSFILTER = "http://aims.fao.org/";
	
	final static public String SELECTEDSCHEMEPROPNAME =  "skos.selected_scheme";
	
	//rdf + xmls
	final static public String PROPERTY = "http://www.w3.org/1999/02/22-rdf-syntax-ns#Property";
	final static public String TYPE = "http://www.w3.org/1999/02/22-rdf-syntax-ns#type";
	final static public String VALUE = "http://www.w3.org/1999/02/22-rdf-syntax-ns#value";
	final static public String DATETIME = "http://www.w3.org/2001/XMLSchema#dateTime";
	final static public String STRINGRDF = "http://www.w3.org/2001/XMLSchema#string";
	
	//rdfs
	final static public String COMMENT = "http://www.w3.org/2000/01/rdf-schema#comment";
	final static public String DOMAIN = "http://www.w3.org/2000/01/rdf-schema#domain";
	final static public String RANGE = "http://www.w3.org/2000/01/rdf-schema#range";
	final static public String SUBPROPERTY = "http://www.w3.org/2000/01/rdf-schema#subPropertyOf";
	final static public String LABEL = "http://www.w3.org/2000/01/rdf-schema#label";
	
	//owl
	final static public String OBJECTPROPERTY = "http://www.w3.org/2002/07/owl#ObjectProperty";
	final static public String DATATYPEPROPERTY = "http://www.w3.org/2002/07/owl#DatatypeProperty";
	final static public String ANNOTATIONPROPERTY = "http://www.w3.org/2002/07/owl#AnnotationProperty";
	final static public String ONTOLOGYPROPERTY = "http://www.w3.org/2002/07/owl#OntologyProperty";
	final static public String SAMEAS = "http://www.w3.org/2002/07/owl#sameAs";
	
	
	//skos
	final static public String CONCEPT_CLASS = "http://www.w3.org/2004/02/skos/core#Concept";
	final static public String NARROWER = "http://www.w3.org/2004/02/skos/core#narrower";
	final static public String BROADER = "http://www.w3.org/2004/02/skos/core#broader";
	final static public String DEFINITION = "http://www.w3.org/2004/02/skos/core#definition";
	final static public String NOTE = "http://www.w3.org/2004/02/skos/core#note";
	final static public String NOTATION = "http://www.w3.org/2004/02/skos/core#notation";
	final static public String SCOPENOTE = "http://www.w3.org/2004/02/skos/core#scopeNote";
	final static public String HASTOCONCEPT = "http://www.w3.org/2004/02/skos/core#hasTopConcept";
	final static public String TOPCONCEPTOF = "http://www.w3.org/2004/02/skos/core#topConceptOf";
	final static public String CONCEPTSCHEME = "http://www.w3.org/2004/02/skos/core#ConceptScheme";
	final static public String INSCHEME= "http://www.w3.org/2004/02/skos/core#inScheme";
	final static public String RELATED = "http://www.w3.org/2004/02/skos/core#related";
	final static public String MAPPINGRELATION = "http://www.w3.org/2004/02/skos/core#mappingRelation";
	final static public String SKOS_PREFLABEL = "http://www.w3.org/2004/02/skos/core#prefLabel";
	final static public String SKOS_ALTLABEL = "http://www.w3.org/2004/02/skos/core#altLabel";
	final static public String SKOS_HIDDENLABEL = "http://www.w3.org/2004/02/skos/core#hiddenLabel";
	
	
	//skosxl
	final static public String LITERALFORM = "http://www.w3.org/2008/05/skos-xl#literalForm";
	final static public String PREFLABEL = "http://www.w3.org/2008/05/skos-xl#prefLabel";
	final static public String HIDDENLABEL = "http://www.w3.org/2008/05/skos-xl#hiddenLabel";
	final static public String ALTLABEL = "http://www.w3.org/2008/05/skos-xl#altLabel";
	final static public String SKOSXLLITERALFORM = "http://www.w3.org/2008/05/skos-xl#literalForm";
	final static public String LABEL_CLASS = "http://www.w3.org/2008/05/skos-xl#Label";
	final static public String LABELRELATION = "http://www.w3.org/2008/05/skos-xl#labelRelation";
	
	
	//agrontology + other
	//final static public String TAKENFROMSOURCE = "http://aims.fao.org/aos/agrontology#takenFromSource"; // old version
	//final static public String HASIMAGESOURCE = "http://aims.fao.org/aos/agrontology#hasImageSource"; // old version
	//final static public String HASSOURCELINK = "http://aims.fao.org/aos/agrontology#hasSourceLink"; // old version
	//final static public String HASIMAGELINK = "http://aims.fao.org/aos/agrontology#hasImageLink"; // old version
	final static public String HASSOURCE = "http://art.uniroma2.it/ontologies/vocbench#hasSource"; // new version
	final static public String HASLINK = "http://art.uniroma2.it/ontologies/vocbench#hasLink"; // new version
	//final static public String HASSTATUS = "http://aims.fao.org/aos/agrontology#hasStatus"; // old version
	final static public String HASSTATUS = "http://art.uniroma2.it/ontologies/vocbench#hasStatus"; // new version
	final static public String ISPARTOFSUBVOCABULARY = "http://aims.fao.org/aos/agrontology#isPartOfSubvocabulary";
	final static public String HASTERMVARIANT = "http://aims.fao.org/aos/agrontology#hasTermVariant";
	final static public String HASTERMTYPE = "http://aims.fao.org/aos/agrontology#hasTermType";
	final static public String AGROVOCCODE = "http://aims.fao.org/aos/agrovoc/AgrovocCode";
	//final static public String CONCEPTDOMAINDATATYPEPROPERTY= 
	//		"http://aims.fao.org/aos/agrontology#conceptDomainDatatypeProperty";
	//final static public String IMAGE = "http://xmlns.com/foaf/0.1/img"; // old version
	final static public String DEPICTION = "http://xmlns.com/foaf/0.1/depiction"; // new version
	final static public String IMAGE_CLASS = "http://xmlns.com/foaf/0.1/Image";
	final static public String CREATED = "http://purl.org/dc/terms/created";
	final static public String MODIFIED = "http://purl.org/dc/terms/modified";
	
	final static public String DIRECTSESAMETYPE = "http://www.openrdf.org/schema/sesame#directType";
	
	private VocBenchResourcesURI() {
	}

}
