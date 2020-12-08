package com.example.linkenup.code;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

public abstract class TextMask {
    public static final String
                                FORMAT_CPF = "###.###.###-##",
                                FORMAT_CTPS = "###### #####-##",
                                FORMAT_CNPJ = "##.###.###/####-##",
                                FORMAT_RG = "##.###.###-#",
                                FORMAT_CE = "##.######-#";

    public static TextWatcher watch(final EditText ediTxt, final String mask) {
        return new TextWatcher() {
            String old = "";
            boolean myChange = false;

            @Override
            public void afterTextChanged(final Editable s) {}

            @Override
            public void beforeTextChanged(final CharSequence s, final int start, final int count, final int after) {}

            @Override
            public void onTextChanged(final CharSequence s, final int start, final int before, final int count) {
                final String str = TextMask.unmask(s.toString());
                String result = "";
                if (str!=old && !myChange) {

                    int i = 0;
                    for (final char nowChar : str.toCharArray()) {

                        while(mask.charAt(i) != '#') {
                            result = result + mask.charAt(i);
                            i++;
                        }
                        result += nowChar;

                        i++;

                        if(i>=mask.length())break;
                    }

                    old = str;
                    myChange =true;
                    ediTxt.setText(result);
                    myChange =false;
                    ediTxt.setSelection(result.length());
                }
            }
        };
    }

    public static String mask(final String s, final String mask){
        String result = "";
            int i = 0;
            for (final char nowChar : s.toCharArray()) {

                while(mask.charAt(i) != '#') {
                    result = result + mask.charAt(i);
                    i++;
                }
                result += nowChar;

                i++;

                if(i>=mask.length())break;
            }
            return  result;
    }

    public static String unmask(final String s) {
        return s.replaceAll("[.]", "").replaceAll("[-]", "").replaceAll("[/]", "").replaceAll("[(]", "").replaceAll("[ ]","").replaceAll("[:]", "").replaceAll("[)]", "");
    }
}