import data.Sku;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

class BinarySearchWithComparator
{
    public static void main(String[] args)
    {
        // Please scroll down to see 'User' class implementation.
        List<Sku> l = new ArrayList<Sku>();
        l.add(new Sku("10", "A"));
        l.add(new Sku("20", "B"));
        l.add(new Sku("30", "C"));

        Comparator<Sku> c = new Comparator<Sku>() {
            public int compare(Sku u1, Sku u2) {
                return u1.id.compareTo(u2.id);
            }
        };

        // Must pass in an object of type 'User' as the key.
        // The key is an 'User' with the 'id' which is been searched for.
        // The 'name' field is not used in the comparison for the binary search,
        // so it can be a dummy value -- here it is omitted with a null.
        //
        // Also note that the List must be sorted before running binarySearch,
        // in this case, the list is already sorted.

        int index = Collections.binarySearch(l, new Sku("20", null), c);
        System.out.println(index);    // Output: 1

        index = Collections.binarySearch(l, new Sku("10", null), c);
        System.out.println(index);    // Output: 0

        index = Collections.binarySearch(l, new Sku("42", null), c);
        System.out.println(index);    // Output: -4
        // See javadoc for meaning of return value.
    }
}
