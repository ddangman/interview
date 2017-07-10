package com.interview.binarysearch;

/**
 * Regular binary search
 */
public class BinarySearch {

    public int search(final int input[], int search) {
        int low = 0;
        int high = input.length - 1;
        int mid;
        while (low <= high) {
            mid = low + ((high - low) / 2);
            if (input[mid] == search) {
                return mid;
            } else if (input[mid] < search) {
                low = mid + 1;
            } else {
                high = mid - 1;
            }
        }
        return -1;
    }

    // while loop exit condition excludes low equal to high
    // since high is reduced to mid instead of mid - 1
    // correct mid index could be missed without checking equal condition
    // due to floor division. For this reason also,
    // low = mid && high = mid - 1 will run infinitely.
    public int binSearchIndex(int a[], int low, int high, int x) {
        while (low < high) {
            int mid = low + (high - low) / 2;
            if (a[mid] < x) {
                low = mid + 1;
            } else if (a[mid] > x) {
                high = mid;
            } else {
                return mid;
            }
        }

        return low;
    }

    /* Recursive Binary Search */
    // Returns index of x if it is present in arr[left..right],
    // else return -1
    int recursiveBinarySearch(int arr[], int left, int right, int x) {
        if (left <= right) {
            int mid = left + (right - left) / 2;

            // If the element is present at the middle itself
            if (arr[mid] == x) {
                return mid;
            }

            // If element is smaller than mid, then it can only
            // be present in left subarray
            if (arr[mid] > x) {
                return recursiveBinarySearch(arr, left, mid - 1, x);
            }

            // Else the element can only be present in right
            // subarray
            return recursiveBinarySearch(arr, mid + 1, right, x);
        }

        // We reach here when element is not present in array
        return -1;
    }

    public static void main(String args[]) {
        BinarySearch bSearch = new BinarySearch();
        final int arr1[] = {1, 2, 4, 5, 7, 8};
        System.out.println(bSearch.search(arr1, -1));
        System.out.println(bSearch.search(arr1, 1));
        System.out.println(bSearch.search(arr1, 8));
        System.out.println(bSearch.search(arr1, 2));
    }
}
