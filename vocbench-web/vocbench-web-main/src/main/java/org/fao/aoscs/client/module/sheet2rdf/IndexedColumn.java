package org.fao.aoscs.client.module.sheet2rdf;

import java.util.Comparator;
import java.util.List;

import com.google.gwt.cell.client.TextCell;
import com.google.gwt.user.cellview.client.Column;

public class IndexedColumn extends Column<List<String>, String>
{
   private Comparator<List<String>> forwardComparator;
   private Comparator<List<String>> reverseComparator;
   private final int index;

   public IndexedColumn(int index)
   {
      super(new TextCell());
      this.index = index;
   }

   @Override
   public String getValue(List<String> object)
   {
      return object.get(index);
   }
   
   public int getIndex(){
       return index;
   }

   public Comparator<List<String>> getComparator(final boolean reverse)
   {
      if (!reverse && forwardComparator != null)
      {
         return forwardComparator;
      }
      if (reverse && reverseComparator != null)
      {
         return reverseComparator;
      }
      Comparator<List<String>> comparator = new Comparator<List<String>>()
      {
         public int compare(List<String> o1, List<String> o2)
         {
            if (o1 == null && o2 == null)
            {
               return 0;
            }
            else if (o1 == null)
            {
               return reverse ? 1 : -1;
            }
            else if (o2 == null)
            {
               return reverse ? -1 : 1;
            }

            // Compare the column value.
            String c1 = getValue(o1);
            String c2 = getValue(o2);
            if (c1 == null && c2 == null)
            {
               return 0;
            }
            else if (c1 == null)
            {
               return reverse ? 1 : -1;
            }
            else if (c2 == null)
            {
               return reverse ? -1 : 1;
            }
            int comparison = ((String) c1).compareTo(c2);
            return reverse ? -comparison : comparison;
         }
      };

      if (reverse)
      {
         reverseComparator = comparator;
      }
      else
      {
         forwardComparator = comparator;
      }
      return comparator;
   }

}