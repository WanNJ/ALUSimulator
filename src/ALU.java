/**
 * 模拟ALU进行整数和浮点数的四则运算
 * @author [请将此处修改为“学号_姓名”]
 *
 */

public class ALU {

	/**
	 * 生成十进制整数的二进制补码表示。<br/>
	 * 例：integerRepresentation("9", 8)
	 * @param number 十进制整数。若为负数；则第一位为“-”；若为正数或 0，则无符号位
	 * @param length 二进制补码表示的长度
	 * @return number的二进制补码表示，长度为length
	 */
	public String integerRepresentation (String number, int length) {
		char[] result = new char[length];
		int decNum = Integer.valueOf(number);

		if(decNum >= 0){
			int remainder;
			for(int i = length - 1;i >= 0;i--){
				remainder = decNum%2;
				decNum = decNum/2;
				if(remainder == 1)
					result[i] = '1';
				else
					result[i] = '0';

			}
		}else if(decNum < 0){
			int sum = -(int)Math.pow(2, length-1);
			result[0] = '1';
			for(int i = 1;i <= length - 1;i++){
				sum = sum + (int)Math.pow(2, length - 1 - i);
				if(sum <= decNum)
					result[i] = '1';
				else{
					result[i] = '0';
					sum = sum - (int)Math.pow(2, length - 1 - i);
				}
			}
		}

		return new String(result);
	}

	/**
	 * 生成十进制浮点数的二进制表示。
	 * 需要考虑 0、反规格化、正负无穷（“+Inf”和“-Inf”）、 NaN等因素，具体借鉴 IEEE 754。
	 * 舍入策略为向0舍入。<br/>
	 * 例：floatRepresentation("11.375", 8, 11)
	 * @param number 十进制浮点数，包含小数点。若为负数；则第一位为“-”；若为正数或 0，则无符号位
	 * @param eLength 指数的长度，取值大于等于 4
	 * @param sLength 尾数的长度，取值大于等于 4
	 * @return number的二进制表示，长度为 1+eLength+sLength。从左向右，依次为符号、指数（移码表示）、尾数（首位隐藏）
	 */
	public String floatRepresentation (String number, int eLength, int sLength) {
		String result = "";
		String exponent = "";
		String significand = "";
		String NaN = "0";
		String Inf = "0";
		String _Inf = "1";
		boolean isMinus = false;
		boolean isTooSmall = false;
		int exp = 0;
		int x = (int)Math.pow(2, eLength - 1) - 1;

		for(int i = 0;i <  eLength;i++){
			NaN = NaN + "1";
		}
		for(int i = 0;i <  sLength;i++){
			NaN = NaN + "1";
		}

		for(int i = 0;i <  eLength;i++){
			Inf = Inf + "1";
		}
		for(int i = 0;i <  sLength;i++){
			Inf = Inf + "0";
		}

		for(int i = 0;i <  eLength;i++){
			_Inf = _Inf + "1";
		}
		for(int i = 0;i <  sLength;i++){
			_Inf = _Inf + "0";
		}


		double Num;
		try{
			Num = Double.parseDouble(number);
		}catch(Exception e){
			return NaN;
		}
		if(Num < 0){
			Num = -Num;
			isMinus = true;
			result = "1";
		}else
			result = "0";

		if(Num < Math.pow( 2, -x))
			isTooSmall = true;


		if(Num == 0){
			for(int i = 0;i < 1+eLength+sLength;i++ ){
				result = result + "0";
			}
			return result;
		}else if(Num > Math.pow( 2, Math.pow(2, eLength) ) * (2 - Math.pow(2,-sLength)) ){
			if(isMinus)
				return Inf;
			else
				return _Inf;
		}else if(!isTooSmall){
			while(Num >= 2){
				Num = Num / 2;
				exp++ ;
			}
			while(Num < 1){
				Num = Num * 2;
				exp--;
			}
			int eNum = (int)Num;
			double sNum = Num - eNum;

			for(int i = 0;i < sLength;i++){
				if(2*sNum >= 1){
					sNum = 2*sNum - 1;
					significand = significand + "1";
				}else{
					sNum = 2*sNum;
					significand = significand + "0";
				}
			}

			exponent = integerRepresentation(String.valueOf(x + exp), eLength + 1).substring(1);
		}else if(isTooSmall){
			for(int i = 0;i < x;i++)
				Num = Num * 2;

			for(int i = 0;i < sLength;i++){
				if(2*Num >= 1){
					Num = 2*Num - 1;
					significand = significand + "1";
				}else{
					Num = 2*Num;
					significand = significand + "0";
				}
			}
		}

		return result + exponent + significand;
	}

	/**
	 * 生成十进制浮点数的IEEE 754表示，要求调用{@link #floatRepresentation(String, int, int) floatRepresentation}实现。<br/>
	 * 例：ieee754("11.375", 32)
	 * @param number 十进制浮点数，包含小数点。若为负数；则第一位为“-”；若为正数或 0，则无符号位
	 * @param length 二进制表示的长度，为32或64
	 * @return number的IEEE 754表示，长度为length。从左向右，依次为符号、指数（移码表示）、尾数（首位隐藏）
	 */
	public String ieee754 (String number, int length) {
		if(length == 32)
			return floatRepresentation(number, 8, 23);
		else
			return floatRepresentation(number, 11, 52);
	}

	/**
	 * 计算二进制补码表示的整数的真值。<br/>
	 * 例：integerTrueValue("00001001")
	 * @param operand 二进制补码表示的操作数
	 * @return operand的真值。若为负数；则第一位为“-”；若为正数或 0，则无符号位
	 */
	public String integerTrueValue (String operand) {
		int result = 0;
		int length = operand.length();
		char[] operandArray = operand.toCharArray();
		for(int i = 0;i < length;i++){
			if(i == 0){
				switch (operandArray[i]) {
				case '0':
					break;
				case '1':
					result = result + (-1)*(int)Math.pow(2, length - 1);
				default:
					break;
				}
			}else{
				switch (operandArray[i]) {
				case '0':
					break;
				case '1':
					result = result + (int)Math.pow(2, length - i - 1);
				default:
					break;
				}
			}

		}
		return String.valueOf(result);
	}

	/**
	 * 计算二进制原码表示的浮点数的真值。<br/>
	 * 例：floatTrueValue("01000001001101100000", 8, 11)
	 * @param operand 二进制表示的操作数
	 * @param eLength 指数的长度，取值大于等于 4
	 * @param sLength 尾数的长度，取值大于等于 4
	 * @return operand的真值。若为负数；则第一位为“-”；若为正数或 0，则无符号位。正负无穷分别表示为“+Inf”和“-Inf”， NaN表示为“NaN”
	 */
	public String floatTrueValue (String operand, int eLength, int sLength) {
		char[] operandArray = operand.toCharArray();
		double result;
		boolean isFZero = true;
		boolean isEZero = true;
		boolean isSZero = true;
		boolean isEOnes = true;
		String exp = operand.substring(1,1+eLength);
		String significand = operand.substring(1+eLength,1+eLength+sLength);

		if(operandArray[0] == '1')
			isFZero = false;

		for(int i = 1;i < eLength + 1;i++){
			if(operandArray[i] == '1'){
				isEZero = false;
				break;
			}
		}

		for(int i = 1;i < eLength + 1;i++){
			if(operandArray[i] == '0'){
				isEOnes = false;
				break;
			}
		}

		for(int i = eLength + 1;i < eLength + sLength + 1;i++){
			if(operandArray[i] == '1'){
				isSZero = false;
				break;
			}
		}

		if(isEZero && isSZero)
			return "0";
		if(isFZero && isEOnes && isSZero)
			return "+Inf";
		if(!isFZero && isEOnes && isSZero)
			return "-Inf";
		if(isEOnes && !isSZero)
			return "NaN";

		if(!isEOnes && !isEZero){
			result = 1.0;
			for(int i = 0;i < sLength;i++){
				if(significand.charAt(i) == '1'){
					result = result + Math.pow(2, -(1+i));
				}
			}
		}else{
			result = 0.0;
			for(int i = 0;i < sLength;i++){
				if(significand.charAt(i) == '1'){
					result = result + Math.pow(2, -(1+i));
				}
			}
		}
		int e = (int) (Double.parseDouble(integerTrueValue("0" + exp)) - Math.pow(2, eLength - 1) + 1);
		result = result * (Math.pow(2, e));

		if(!isFZero)
			result = - result;

		return String.valueOf(result);
	}

	/**
	 * 按位取反操作。<br/>
	 * 例：negation("00001001")
	 * @param operand 二进制表示的操作数
	 * @return operand按位取反的结果
	 */
	public String negation (String operand) {
		char[] operandArray = operand.toCharArray();
		String result = "";
		for(int i = 0;i < operand.length();i++){
			if(operandArray[i] == '0')
				result = result + "1";
			else
				result = result + "0";
		}
		return result;
	}

	/**
	 * 左移操作。<br/>
	 * 例：leftShift("00001001", 2)
	 * @param operand 二进制表示的操作数
	 * @param n 左移的位数
	 * @return operand左移n位的结果
	 */
	public String leftShift (String operand, int n) {
		char[] operandArray = operand.toCharArray();
		String result = "";

		for(int i = n;i < operand.length();i++){
			if(operandArray[i] == '0')
				result = result + "0";
			else
				result = result + "1";
		}
		for(int i = 0;i < n;i++){
			result = result + "0";
		}

		return result;
	}

	/**
	 * 逻辑右移操作。<br/>
	 * 例：logRightShift("11110110", 2)
	 * @param operand 二进制表示的操作数
	 * @param n 右移的位数
	 * @return operand逻辑右移n位的结果
	 */
	public String logRightShift (String operand, int n) {

		char[] operandArray = operand.toCharArray();
		String result = "";

		for(int i = 0;i < n;i++){
			result = result + "0";
		}

		for(int i = 0;i < (operand.length() - n);i++){
			if(operandArray[i] == '0')
				result = result + "0";
			else
				result = result + "1";
		}


		return result;
	}

	/**
	 * 算术右移操作。<br/>
	 * 例：logRightShift("11110110", 2)
	 * @param operand 二进制表示的操作数
	 * @param n 右移的位数
	 * @return operand算术右移n位的结果
	 */
	public String ariRightShift (String operand, int n) {
		char[] operandArray = operand.toCharArray();
		String result = "";

		if(operandArray[0] == '0'){
			for(int i = 0;i < n;i++){
				result = result + "0";
			}
		}else{
			for(int i = 0;i < n;i++){
				result = result + "1";
			}
		}

		for(int i = 0;i < (operand.length() - n);i++){
			if(operandArray[i] == '0')
				result = result + "0";
			else
				result = result + "1";
		}


		return result;
	}

	/**
	 * 全加器，对两位以及进位进行加法运算。<br/>
	 * 例：fullAdder('1', '1', '0')
	 * @param x 被加数的某一位，取0或1
	 * @param y 加数的某一位，取0或1
	 * @param c 低位对当前位的进位，取0或1
	 * @return 相加的结果，用长度为2的字符串表示，第1位表示进位，第2位表示和
	 */
	public String fullAdder (char x, char y, char c) {
		String result = "";
		if((x == '1' && y== '1')||(y == '1' && c == '1')||(x == '1' && c == '1')){
			result = result + "1";
			if(x == '0' || y == '0' || c == '0')
				result = result + "0";
			else
				result = result + "1";
		}
		else{
			result = result + "0";
			if(x == '1' || y == '1' || c == '1')
				result = result + "1";
			else
				result = result + "0";
		}
		return result;
	}

	/**
	 * 4位先行进位加法器。要求采用{@link #fullAdder(char, char, char) fullAdder}来实现<br/>
	 * 例：claAdder("1001", "0001", '1')
	 * @param operand1 4位二进制表示的被加数
	 * @param operand2 4位二进制表示的加数
	 * @param c 低位对当前位的进位，取0或1
	 * @return 长度为5的字符串表示的计算结果，其中第1位是最高位进位，后4位是相加结果，其中进位不可以由循环获得
	 */
	public String claAdder (String operand1, String operand2, char c) {
		String result = "";

		char[] op1 = operand1.toCharArray();
		char[] op2 = operand2.toCharArray();

		char P1 = orGate(op1[3],op2[3]);
		char P2 = orGate(op1[2],op2[2]);
		char P3 = orGate(op1[1],op2[1]);
		char P4 = orGate(op1[0],op2[0]);

		char G1 = andGate(op1[3],op2[3]);
		char G2 = andGate(op1[2],op2[2]);
		char G3 = andGate(op1[1],op2[1]);
		char G4 = andGate(op1[0],op2  [0]);

		char C1 = orGate(G1, andGate(P1,c));
		char C2 = orGate(G2, andGate(P2,G1), andGate(P2,P1,c));
		char C3 = orGate(G3, andGate(P3,G2), andGate(P3,P2,G1), andGate(P3,P2,P1,c));
		char C4 = orGate(G4, andGate(P4,G3), andGate(P4,P3,G2), andGate(P4,P3,P2,G1), andGate(P4,P3,P2,P1,c));

		char rest = C4;
		if(rest == '!')
			System.out.println("HAHA.");

//		for some reason ,C4 is not necessary,so 428-430 is just joking
		result = result + fullAdder(op1[0],op2[0],C3);

		if(fullAdder(op1[1],op2[1],C2).toCharArray()[1] == '0')
			result = result + "0";
		else
			result = result + "1";

		if(fullAdder(op1[2],op2[2],C1).toCharArray()[1] == '0')
			result = result + "0";
		else
			result = result + "1";

		if(fullAdder(op1[3],op2[3],c).toCharArray()[1] == '0')
			result = result + "0";
		else
			result = result + "1";

		return result;
	}

	/**
	 * 加一器，实现操作数加1的运算。
	 * 需要采用与门、或门、异或门等模拟.
	 * 不可以直接调用{@link #fullAdder(char, char, char) fullAdder}、
	 * {@link #claAdder(String, String, char) claAdder}、
	 * {@link #adder(String, String, char, int) adder}、
	 * {@link #integerAddition(String, String, int) integerAddition}方法。<br/>
	 * 例：oneAdder("00001001")
	 * @param operand 二进制补码表示的操作数
	 * @return operand加1的结果，长度为operand的长度加1，其中第1位指示是否溢出（溢出为1，否则为0），其余位为相加结果
	 */
	public String oneAdder (String operand) {
		String result = "";
		int length = operand.length();
		char[] op = operand.toCharArray();
		char x;
		if(andGate(op[length - 1],'1') == '1'){
			x = '1';
			result = result + "0";
		}
		else{
			x = '0';
			if(orGate(op[length - 1],'1') == '0')
				result = result + "0";
			else
				result = result + "1";
		}


		for(int i = length - 2;i >= 0;i--){
			if(andGate(op[i],x) == '1'){
				x = '1';
				result = result + "0";
			}
			else{
				if(orGate(op[i],x) == '0')
					result = result + "0";
				else
					result = result + "1";
				x = '0';
			}
		}

		StringBuilder builder = new StringBuilder(result);
		String finalResult = builder.reverse().toString();
		if(finalResult.toCharArray()[0] == '1' && op[0] == '0')
			result = result + "1";
		else
			result =result + "0";
		builder = new StringBuilder(result);
		return  builder.reverse().toString();
	}

	/**
	 * 加法器，要求调用{@link #claAdder(String, String, char)}方法实现。<br/>
	 * 例：adder("0100", "0011", ‘0’, 8)
	 * @param operand1 二进制补码表示的被加数
	 * @param operand2 二进制补码表示的加数
	 * @param c 最低位进位
	 * @param length 存放操作数的寄存器的长度，为4的倍数。length不小于操作数的长度，当某个操作数的长度小于length时，需要在高位补符号位
	 * @return 长度为length+1的字符串表示的计算结果，其中第1位指示是否溢出（溢出为1，否则为0），后length位是相加结果
	 */
	public String adder (String operand1, String operand2, char c, int length) {
		String result = "";
		String op1 = integerRepresentation(integerTrueValue(operand1),length);
		String op2 = integerRepresentation(integerTrueValue(operand2),length);

		int size = length/4;
		String[] results = new String[size];
		char C = c;
		for(int i = size - 1;i >= 0;i--){
			results[i] = claAdder(op1.substring(i*4,(i+1)*4),op2.substring(i*4,(i+1)*4),C).substring(1);
			C = claAdder(op1.substring(i*4,(i+1)*4),op2.substring(i*4,(i+1)*4),C).toCharArray()[0];
		}

		if((operand1.toCharArray()[0] == '0' && operand2.toCharArray()[0] == '0' && results[0].toCharArray()[0] == '1')
				||(operand1.toCharArray()[0] == '1' && operand2.toCharArray()[0] == '1' && results[0].toCharArray()[0] == '0'))
			result = result + "1";
		else
			result = result + "0";

		for(String s:results){
			result = result + s;
		}

		return result;
	}

	/**
	 * 整数加法，要求调用{@link #adder(String, String, char, int) adder}方法实现。<br/>
	 * 例：integerAddition("0100", "0011", 8)
	 * @param operand1 二进制补码表示的被加数
	 * @param operand2 二进制补码表示的加数
	 * @param length 存放操作数的寄存器的长度，为4的倍数。length不小于操作数的长度，当某个操作数的长度小于length时，需要在高位补符号位
	 * @return 长度为length+1的字符串表示的计算结果，其中第1位指示是否溢出（溢出为1，否则为0），后length位是相加结果
	 */
	public String integerAddition (String operand1, String operand2, int length) {
		return adder(operand1,operand2,'0',length);
	}

	/**
	 * 整数减法，可调用{@link #adder(String, String, char, int) adder}方法实现。<br/>
	 * 例：integerSubtraction("0100", "0011", 8)
	 * @param operand1 二进制补码表示的被减数
	 * @param operand2 二进制补码表示的减数
	 * @param length 存放操作数的寄存器的长度，为4的倍数。length不小于操作数的长度，当某个操作数的长度小于length时，需要在高位补符号位
	 * @return 长度为length+1的字符串表示的计算结果，其中第1位指示是否溢出（溢出为1，否则为0），后length位是相减结果
	 */
	public String integerSubtraction (String operand1, String operand2, int length) {
		int trueValue = -Integer.parseInt(integerTrueValue(operand2));
		String value = integerRepresentation(String.valueOf(trueValue), length);
		return adder(operand1,value,'0',length);
	}

	/**
	 * 整数乘法，使用Booth算法实现，可调用{@link #adder(String, String, char, int) adder}等方法。<br/>
	 * 例：integerMultiplication("0100", "0011", 8)
	 * @param operand1 二进制补码表示的被乘数
	 * @param operand2 二进制补码表示的乘数
	 * @param length 存放操作数的寄存器的长度，为4的倍数。length不小于操作数的长度，当某个操作数的长度小于length时，需要在高位补符号位
	 * @return 长度为length+1的字符串表示的相乘结果，其中第1位指示是否溢出（溢出为1，否则为0），后length位是相乘结果
	 */
	public String integerMultiplication (String operand1, String operand2, int length) {
		String result = "0";
		String finalResult = "";
		String X = integerRepresentation(integerTrueValue(operand1),operand1.length());
		String _X = integerRepresentation(String.valueOf(-Integer.parseInt(integerTrueValue(operand1))),operand1.length());
		String Y = operand2;

		for(int i = 0;i < length - operand1.length();i++){
			X = X + "0";
			_X = _X  + "0";
		}


		char[] Yarray = Y.toCharArray();
		for(int i = operand2.length() - 1;i >= 0;i--){
			if(i == operand2.length() - 1){
				if(('0' - Yarray[i]) == -1)
					result = integerAddition(result, _X, length).substring(1);
			}else{
				if((Yarray[i+1] - Yarray[i]) == -1)
					result = integerAddition(result, _X, length).substring(1);
				else if((Yarray[i+1] - Yarray[i]) == 1)
					result = integerAddition(result, X, length).substring(1);
			}

			if(leftShift(ariRightShift(result, 1), 1).equals(result) == false)
				finalResult = "1";
			result = ariRightShift(result, 1);
		}

		if(finalResult.equals("1"))
			return finalResult + result;
		else
			return "0" + result;
	}

	/**
	 * 整数的不恢复余数除法，可调用{@link #adder(String, String, char, int) adder}等方法实现。<br/>
	 * 例：integerDivision("0100", "0011", 8)
	 * @param operand1 二进制补码表示的被除数
	 * @param operand2 二进制补码表示的除数
	 * @param length 存放操作数的寄存器的长度，为4的倍数。length不小于操作数的长度，当某个操作数的长度小于length时，需要在高位补符号位
	 * @return 长度为2*length+1的字符串表示的相除结果，其中第1位指示是否溢出（溢出为1，否则为0），其后length位为商，最后length位为余数
	 */
	public String integerDivision (String operand1, String operand2, int length) {
		String result = "";
		String quotient = "";
		Boolean x = operand1.toCharArray()[0] == operand2.toCharArray()[0];
		if(isZero(operand1)){
			if(isZero(operand2)){
				return "NaN";
			}else{
				for(int i = 0;i < 2*length + 1;i++){
					result = result + "0";
				}
				return result;
			}
		}else if(isZero(operand2))
			return "Exception";

		String remainder = integerRepresentation(integerTrueValue(operand1),2*length);
		String divisor = integerRepresentation(integerTrueValue(operand2),length);

		remainder = leftShift(remainder, 1);

		for(int i = 0; i < length - 1;i++){
			if(x){
				if(integerSubtraction(remainder.substring(0, length),divisor,length).toCharArray()[1] == remainder.toCharArray()[0] ){
					remainder = integerSubtraction(remainder.substring(0, length),divisor,length).substring(1) + remainder.substring(length);
					remainder = leftShift(remainder, 1);
					quotient = quotient + "1";
				}else{
					remainder = leftShift(remainder, 1);
					quotient = quotient + "0";
				}
			}else{
				if(integerAddition(remainder.substring(0, length),divisor,length).toCharArray()[1] == remainder.toCharArray()[0] ){
					remainder = integerAddition(remainder.substring(0, length),divisor,length).substring(1) + remainder.substring(length);
					remainder = leftShift(remainder, 1);
					quotient = quotient + "1";
				}else{
					remainder = leftShift(remainder, 1);
					quotient = quotient + "0";
				}
			}
		}

		if(x){
			if(integerSubtraction(remainder.substring(0, length),divisor,length).toCharArray()[1] == remainder.toCharArray()[0] ){
				remainder = integerSubtraction(remainder.substring(0, length),divisor,length).substring(1) + remainder.substring(length);
				quotient = quotient + "1";
			}else{
				quotient = quotient + "0";
			}
		}else{
			if(integerAddition(remainder.substring(0, length),divisor,length).toCharArray()[1] == remainder.toCharArray()[0] ){
				remainder = integerAddition(remainder.substring(0, length),divisor,length).substring(1) + remainder.substring(length);
				quotient = quotient + "1";
			}else{
				quotient = quotient + "0";
			}

			quotient = oneAdder(negation(quotient)).substring(1);
		}

		if(quotient.toCharArray()[0] != operand1.toCharArray()[0])
			result = "1";
		else
			result = "0";

		return result + quotient + remainder.substring(0,length);
	}

	/**
	 * 带符号整数加法，可以调用{@link #adder(String, String, char, int) adder}等方法，
	 * 但不能直接将操作数转换为补码后使用{@link #integerAddition(String, String, int) integerAddition}、
	 * {@link #integerSubtraction(String, String, int) integerSubtraction}来实现。<br/>
	 * 例：signedAddition("1100", "1011", 8)
	 * @param operand1 二进制原码表示的被加数，其中第1位为符号位
	 * @param operand2 二进制原码表示的加数，其中第1位为符号位
	 * @param length 存放操作数的寄存器的长度，为4的倍数。length不小于操作数的长度（不包含符号），当某个操作数的长度小于length时，需要将其长度扩展到length
	 * @return 长度为length+2的字符串表示的计算结果，其中第1位指示是否溢出（溢出为1，否则为0），第2位为符号位，后length位是相加结果
	 */
	public String signedAddition (String operand1, String operand2, int length) {
		String result = "";
		String op1;
		String op2;

		if(operand1.toCharArray()[0] == '1'){
			op1 = integerRepresentation("-" + integerTrueValue("0" + operand1.substring(1)),length);
		}else{
			op1 = integerRepresentation(integerTrueValue(operand1),length);
		}

		if(operand2.toCharArray()[0] == '1'){
			op2 = integerRepresentation("-" + integerTrueValue("0" + operand2.substring(1)),length);
		}else{
			op2 = integerRepresentation(integerTrueValue(operand2),length);
		}

		String num = integerTrueValue(adder(op1,op2,'0',length+4).substring(1));

		int trueValue;
		if(num.toCharArray()[0] == '-')
			trueValue = -Integer.parseInt(num);
		else
			trueValue = Integer.parseInt(num);

		int max = (int)Math.pow(2, length - 1);

		if(trueValue >= max)
			result = "1";
		else
			result = "0";

		if(result.equals("1"))
			result = result + operand1.substring(0,1);
		else
			result = result + adder(op1,op2,'0',length+4).substring(1,2);

		result = result + integerRepresentation( String.valueOf(trueValue), length+1).substring(1);
		return result;
	}


	/**
	 * 浮点数加法，可调用{@link #signedAddition(String, String, int) signedAddition}等方法实现。<br/>
	 * 例：floatAddition("00111111010100000", "00111111001000000", 8, 8, 8)
	 * @param operand1 二进制表示的被加数
	 * @param operand2 二进制表示的加数
	 * @param eLength 指数的长度，取值大于等于 4
	 * @param sLength 尾数的长度，取值大于等于 4
	 * @param gLength 保护位的长度
	 * @return 长度为2+eLength+sLength的字符串表示的相加结果，其中第1位指示是否指数上溢（溢出为1，否则为0），其余位从左到右依次为符号、指数（移码表示）、尾数（首位隐藏）。舍入策略为向0舍入
	 */
	public String floatAddition (String operand1, String operand2, int eLength, int sLength, int gLength) {
		String result = "";
		String exp = "";
		String signedValue = "";
		String zero = "";
		String protectBits = "";
		for(int i = 0;i < gLength;i++){
			protectBits = protectBits + "0";
		}
		for(int i = 0;i < 2+eLength+sLength;i++){
			zero = zero + "0";
		}
		int num = 0;

		String exp1 = operand1.substring(1,1+eLength);
		String exp2 = operand2.substring(1,1+eLength);
		String signedValue1 = operand1.substring(0,1) + operand1.substring(1+eLength,1 + eLength + sLength) + protectBits;
		String signedValue2 = operand2.substring(0,1) + operand2.substring(1+eLength,1 + eLength + sLength) + protectBits;

		if(isZero(operand1.substring(1))){
			return "0" + operand2;
		}else if(isZero(operand2.substring(1))){
			return "0" + operand1;
		}

		int ex1 = Integer.parseInt(integerTrueValue(exp1));
		int ex2 = Integer.parseInt(integerTrueValue(exp2));

		if(ex1 > ex2){
			num = ex1 - ex2;
			exp = exp1;
			if(isZero(exp2))
				signedValue2 = operand2.substring(0,1) + logRightShift(signedValue2.substring(1), num);
			else
				signedValue2 = operand2.substring(0,1) + logRightShift("1" + signedValue2.substring(1 ,sLength+gLength), num - 1);

			if(isZero(signedValue2.substring(1)))
				return "0" + operand1;
		}
		else if(ex1 < ex2){
			num = ex2 - ex1;
			exp = exp2;
			if(isZero(exp1))
				signedValue1 = operand1.substring(0,1) + logRightShift(signedValue1.substring(1),num);
			else
				signedValue1 = operand1.substring(0,1) + logRightShift("1" + signedValue1.substring(1 ,sLength+gLength), num - 1);

			if(isZero(signedValue1.substring(1)))
				return "0" + operand2;
		}else{
			if(!isZero(exp1) && !isZero(exp2)){
				signedValue1 = operand1.substring(0,1) + "1" + signedValue1.substring(1 , sLength+gLength);
				signedValue2 = operand2.substring(0,1) + "1" + signedValue2.substring(1 , sLength+gLength);
			}

			exp = exp1;
		}
		int distance = signedValue1.length()/4 * 4 + 4 - signedValue1.length();

		for(int i = 0;i < distance;i++){
			signedValue1 = signedValue1 + "0";
			signedValue2 = signedValue2 + "0";
		}
		signedValue = signedAddition(signedValue1, signedValue2, signedValue1.length());
//		System.out.println(signedValue);
		if(isZero(signedValue.substring(2,2+sLength)))
			return zero;

		if(signedValue.toCharArray()[0] == '1'){
			signedValue = signedValue.substring(1,2) + signedValue.substring(3);
			if(oneAdder(exp).toCharArray()[0] == '1'){
				result ="1";
			}else
				result ="0";

			exp = oneAdder(exp).substring(1);
		}else if(signedValue.charAt(2) == '0'){
			signedValue = signedValue.substring(1,2) + signedValue.substring(3);
			while(signedValue.charAt(1) != '1'){
				signedValue = signedValue.substring(0,1) + leftShift(signedValue, 1).substring(1);
				exp = integerSubtraction("0" + exp , "01", ((exp.length() + 1) / 4 + 1)* 4);
				if(exp.toCharArray()[1] == '1'){
					result ="1";
				}else
					result ="0";
				exp = exp.substring(exp.length() - eLength);
			}

			signedValue = signedValue.substring(0,1) + signedValue.substring(2);
		}else
			signedValue = signedValue.substring(1,2) + signedValue.substring(4);

		result = result + signedValue.substring(0,1) + exp + signedValue.substring(1,1+sLength);

		return result;
	}

	/**
	 * 浮点数减法，可调用{@link #floatAddition(String, String, int, int, int) floatAddition}方法实现。<br/>
	 * 例：floatSubtraction("00111111010100000", "00111111001000000", 8, 8, 8)
	 * @param operand1 二进制表示的被减数
	 * @param operand2 二进制表示的减数
	 * @param eLength 指数的长度，取值大于等于 4
	 * @param sLength 尾数的长度，取值大于等于 4
	 * @param gLength 保护位的长度
	 * @return 长度为2+eLength+sLength的字符串表示的相减结果，其中第1位指示是否指数上溢（溢出为1，否则为0），其余位从左到右依次为符号、指数（移码表示）、尾数（首位隐藏）。舍入策略为向0舍入
	 */
	public String floatSubtraction (String operand1, String operand2, int eLength, int sLength, int gLength) {
		if(operand2.toCharArray()[0] == '0')
			return floatAddition(operand1, "1" + operand2.substring(1), eLength, sLength, gLength);
		else
			return floatAddition(operand1, "0" + operand2.substring(1), eLength, sLength, gLength);
	}

	/**
	 * 浮点数乘法，可调用{@link #integerMultiplication(String, String, int) integerMultiplication}等方法实现。<br/>
	 * 例：floatMultiplication("00111110111000000", "00111111000000000", 8, 8)
	 * @param operand1 二进制表示的被乘数
	 * @param operand2 二进制表示的乘数
	 * @param eLength 指数的长度，取值大于等于 4
	 * @param sLength 尾数的长度，取值大于等于 4
	 * @return 长度为2+eLength+sLength的字符串表示的相乘结果,其中第1位指示是否指数上溢（溢出为1，否则为0），其余位从左到右依次为符号、指数（移码表示）、尾数（首位隐藏）。舍入策略为向0舍入
	 */
	public String floatMultiplication (String operand1, String operand2, int eLength, int sLength) {
		String zero = "";
		String dis = "0";
		String value = "";
		String result = "";


		String exp1 = operand1.substring(1,1+eLength);
		String exp2 = operand2.substring(1,1+eLength);
		String value1 =operand1.substring(1+eLength,1 + eLength + sLength);
		String value2 =operand2.substring(1+eLength,1 + eLength + sLength);

		for(int i = 0;i<2+eLength+sLength;i++){
			zero = zero + "0";
		}

		for(int i = 0;i < eLength - 1;i++){
			dis = dis + "1";
		}

		if(isZero(operand1.substring(1)))
			return zero;
		else if(isZero(operand2.substring(1)))
			return zero;

		String exponent = integerSubtraction(integerAddition("0" + exp1, "0" + exp2,(eLength/4 + 1)*4), dis, (eLength/4 + 1)*4);

		String finalExp = exponent.substring((eLength/4 + 1)*4 - eLength + 1);

		int exp = Integer.parseInt(integerTrueValue(exponent.substring(1)));

		if(exp < 0 || exp > Integer.parseInt(integerTrueValue("01" + dis.substring(1))))
			result = "1";
		else
			result = "0";

		if(isZero(exp1)){
			value = integerMultiplication("00" + value1,"01" + value2, 2*( (sLength+2) / 4 + 1) * 4 ).substring(( (sLength+2) / 4 + 1) * 4 - sLength);
		}else if(isZero(exp2)){
			value = integerMultiplication("01" + value1,"00" + value2, 2*( (sLength+2) / 4 + 1) * 4 ).substring(( (sLength+2) / 4 + 1) * 4 - sLength);
		}else{
			value = integerMultiplication("01" + value1,"01" + value2, 2*( (sLength+2) / 4 + 1) * 4 ).substring(( (sLength+2) / 4 + 1) * 4 - sLength);
		}

		for(int i = 0;i < 2*( (sLength+1) / 4 + 1) * 4 -1;i++){
			if(value.charAt(0) != '1'){
				value = leftShift(value,1);
				if(integerSubtraction("0" + finalExp,"01", (1 + finalExp.length())/4 * 4 + 4).charAt(1) == '1'){
					result = "1";
					break;
				}else{
					finalExp = integerSubtraction("0" + finalExp,"01", (1 + finalExp.length())/4 * 4 + 4).substring((1 + finalExp.length())/4 * 4 + 4 - eLength + 1);
				}
			}else if(value.charAt(0) == '1'){
				value = leftShift(value,1);
				break;
			}
		}

		value = value.substring(0,sLength);
		result = result + String.valueOf(xorGate(operand1.charAt(0),operand1.charAt(0)));
		result = result + finalExp + value;
		return result;
	}

	/**
	 * 浮点数除法，可调用{@link #integerDivision(String, String, int) integerDivision}等方法实现。<br/>
	 * 例：floatDivision("00111110111000000", "00111111000000000", 8, 8)
	 * @param operand1 二进制表示的被除数
	 * @param operand2 二进制表示的除数
	 * @param eLength 指数的长度，取值大于等于 4
	 * @param sLength 尾数的长度，取值大于等于 4
	 * @return 长度为2+eLength+sLength的字符串表示的相乘结果,其中第1位指示是否指数上溢（溢出为1，否则为0），其余位从左到右依次为符号、指数（移码表示）、尾数（首位隐藏）。舍入策略为向0舍入
	 */
	public String floatDivision (String operand1, String operand2, int eLength, int sLength) {
		String zero = "";
		String dis = "0";
		String value = "";
		String result = "";


		String exp1 = operand1.substring(1,1+eLength);
		String exp2 = operand2.substring(1,1+eLength);
		String value1 =operand1.substring(1+eLength,1 + eLength + sLength);
		String value2 =operand2.substring(1+eLength,1 + eLength + sLength);

		for(int i = 0;i<2+eLength+sLength;i++){
			zero = zero + "0";
		}

		for(int i = 0;i < eLength - 1;i++){
			dis = dis + "1";
		}

		if(isZero(operand1.substring(1)))
			return zero;
		else if(isZero(operand2.substring(1)))
			return zero;

		String exponent = integerAddition(integerSubtraction("0" + exp1, "0" + exp2,(eLength/4 + 1)*4), dis, (eLength/4 + 1)*4);

		String finalExp = exponent.substring((eLength/4 + 1)*4 - eLength + 1);

		int exp = Integer.parseInt(integerTrueValue(exponent.substring(1)));

		if(exp < 0 || exp > Integer.parseInt(integerTrueValue("01" + dis.substring(1))))
			result = "1";
		else
			result = "0";

		if(isZero(exp1)){
			value = newIntegerDivision("0" + value1,"1" + value2);
		}else if(isZero(exp2)){
			value = newIntegerDivision("1" + value1,"0" + value2);
		}else{
			value = newIntegerDivision("1" + value1,"1" + value2);
		}

		for(int i = 0;i < ( (sLength+1) / 4 + 1) * 4 -1;i++){
			if(value.charAt(0) != '1'){
				value = leftShift(value,1);
				if(integerSubtraction("0" + finalExp,"01", (1 + finalExp.length())/4 * 4 + 4).charAt(1) == '1'){
					result = "1";
					break;
				}else{
					finalExp = integerSubtraction("0" + finalExp,"01", (1 + finalExp.length())/4 * 4 + 4).substring((1 + finalExp.length())/4 * 4 + 4 - eLength + 1);
				}
			}else if(value.charAt(0) == '1'){
				value = leftShift(value,1);
				break;
			}
		}

		value = value.substring(0,sLength);
		result = result + String.valueOf(xorGate(operand1.charAt(0),operand1.charAt(0)));
		result = result + finalExp + value;
		return result;
	}

	public String stringArrayToString(String[] s){
		String result = "";
		for(String x:s){
			result = result + x;
		}
		return result;
	}

	public char andGate(char... operandArray){
		for(char x:operandArray){
			if(x == '0'){
				return '0';
			}
		}
		return '1';
	}

	public char orGate(char...operandArray){
		for(char x:operandArray){
			if(x == '1'){
				return '1';
			}
		}
		return '0';
	}

	public char xorGate(char...operandArray){
		int num = 0;
		for(char x:operandArray){
			if(x == '1'){
				num++;
			}
		}
		if(num%2 == 0)
			return '0';
		else
			return '1';
	}

	public boolean isZero(String s){
		int length = s.length();
		char[] sArray = s.toCharArray();
		for(int i = 0;i < length;i++){
			if(sArray[i] == '1')
				return false;
		}
		return true;
	}

	public String newIntegerDivision(String operand1,String operand2){
		String quotient = "";
		String remainder = operand1;
		String divisor = operand2;
		int length = remainder.length();
		for(int i = 0;i < operand1.length();i++){
			if(integerSubtraction("0"+remainder, "0" + divisor,4*(length/4 + 1)).charAt(1) == '1'){
				remainder = leftShift(remainder, 1);
				quotient = quotient + "0";
			}else{
				remainder = integerSubtraction(remainder, divisor, 4*(length/4 + 1)).substring(4*(length/4 + 1) - length);
				remainder = leftShift(remainder, 1);
				quotient = quotient + "1";
			}
		}

		return quotient;
	}
}
