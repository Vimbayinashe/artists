public class Guard {

    public static class Against {

        public static boolean InvalidInt(String value) {
            try {
                Integer.parseInt(value);
                return false;
            } catch (NumberFormatException e) {
                return true;
            }
        }

    }

}
