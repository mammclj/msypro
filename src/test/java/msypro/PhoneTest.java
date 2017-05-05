package msypro;

import com.msymobile.www.commons.utils.PhoneUtil;

public class PhoneTest {

	public static void main(String[] args) {
		String rs = PhoneUtil.getGeo("18888888888", "86");
		System.out.println(rs);
	}

}
