package Sorting;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class MergeSortImpl {
	public static void main(String[] args) throws Exception {
		List<Integer> list = Arrays.asList(1,3,5,7,9,2,4);
		ExecutorService executorService = Executors.newCachedThreadPool();
		MergeSorter mergeSorter = new MergeSorter(list, executorService);
		Future<List<Integer>> resFuture = executorService.submit(mergeSorter);
		List<Integer> res = resFuture.get();
		System.out.print(res);
	}
}

class MergeSorter implements Callable<List<Integer>> {
	List<Integer> list;
	ExecutorService executorService;
	MergeSorter(List<Integer> list,ExecutorService executorService){
		this.list = list;
		this.executorService = executorService;
	}

	@Override
	public List<Integer> call() throws Exception {
		if (list.size() <= 1) {
            return list;
        }
		
		List<Integer> left = new ArrayList<>();
		List<Integer> right = new ArrayList<>();
		int mid=(0+list.size())/2;
		for(int i =0;i<mid;i++) {
			left.add(list.get(i));
		}
		for(int i =mid;i<list.size();i++) {
			right.add(list.get(i));
		}
		Future<List<Integer>> leftFuture = executorService.submit(new MergeSorter(left, executorService));
		Future<List<Integer>> rightFuture = executorService.submit(new MergeSorter(right, executorService));
		List<Integer> leftSorted = leftFuture.get();
		List<Integer> rightSorted = rightFuture.get();
		int i=0,j=0;
		List<Integer> res = new ArrayList<>();
		while(i<leftSorted.size() && j<rightSorted.size()) {
			if(leftSorted.get(i)<rightSorted.get(j))
				res.add(leftSorted.get(i++));
			else
				res.add(rightSorted.get(j++));
		}
		while(i<leftSorted.size())
			res.add(leftSorted.get(i++));
		while(j<rightSorted.size())
			res.add(rightSorted.get(j++));
		return res;
	}
	
}
