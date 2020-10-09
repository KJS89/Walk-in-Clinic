package com.example.walkinclinics;

import android.text.Editable;
import android.text.TextWatcher;

import java.util.regex.Pattern;

public class ValidatorUsername implements TextWatcher {


        public static final Pattern UpperCasePatten = Pattern.compile("[A-Z ]");
        public static final Pattern lowerCasePatten = Pattern.compile("[a-z ]");


        private boolean uIsValid = false;
        public boolean isValid() {
            return uIsValid;
        }

        public static boolean isValidUsername(CharSequence username)
        {
            if (username == null)
            {
                return false;
            }
            if (username.length() > 8 )
            {
                return false;
            }

            if ( !UpperCasePatten.matcher(username).find() )
            {
                return false;
            }
            if (!lowerCasePatten.matcher(username).find())
            {
                return false;
            }

            return true;

        }


        @Override
        public void afterTextChanged(Editable editableText)
        {
            uIsValid = isValidUsername(editableText);
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after)
        {
            /*No-op*/
        }

        @Override
        final public void onTextChanged(CharSequence s, int start, int before, int count)
        {
            /*No-op*/
        }

}
