package com.blubank.interviewpostquestion.util;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class TestUtil {

	public static <T> Set<T> setOf(T... objs) {
		Set<T> set = new HashSet<>();
		for (T item : objs)
			set.add(item);
		return set;
	}

	public static <T> List<T> listOf(T... objs) {
		List<T> set = new ArrayList<>();
		for (T item : objs)
			set.add(item);
		return set;
	}

}
