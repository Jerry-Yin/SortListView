package jiudeng.me.sortlistview;

import java.util.Comparator;

/**
 * 
 * @author xiaanming
 * 比较器 重写 compare（）方法，返回3种植
 *  负数： o1 < o2
 *   0	:  o1 = 02
 *  正数： o1 > 02
 */
public class PinyinComparator implements Comparator<SortModel> {

	public int compare(SortModel o1, SortModel o2) {
		if (o1.getSortLetters().equals("@")
				|| o2.getSortLetters().equals("#")) {
			return -1;
		} else if (o1.getSortLetters().equals("#")
				|| o2.getSortLetters().equals("@")) {
			return 1;
		} else {
			return o1.getSortLetters().compareTo(o2.getSortLetters());
		}
	}

}
