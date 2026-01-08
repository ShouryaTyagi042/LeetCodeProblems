class LargestOddNumber {
    public static void main(String[] args) {
        String test = "52" ;
        System.out.println(largestOddNumber(test) );
    }


    public static String largestOddNumber(String num) {
        for (int i = num.length() - 1; i >= 0; i--) {
            int digit = num.charAt(i) - '0';
            if (digit % 2 != 0) {
                return num.substring(0, i + 1);
            }
        }
        return "";
    }
}
