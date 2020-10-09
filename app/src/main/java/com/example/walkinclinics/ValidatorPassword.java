package com.example.walkinclinics;

import android.text.Editable;
import android.text.TextWatcher;

import java.util.regex.Pattern;

public class ValidatorPassword implements TextWatcher
{
    public static final Pattern specailCharPatten = Pattern.compile("[^a-z0-9 ]", Pattern.CASE_INSENSITIVE);
    public static final Pattern UpperCasePatten = Pattern.compile("[A-Z ]");
    public static final Pattern lowerCasePatten = Pattern.compile("[a-z ]");
    public static final Pattern digitCasePatten = Pattern.compile("[0-9 ]");

    private boolean mIsValid = false;
    public boolean isValid() {
        return mIsValid;
    }

    public static boolean isValidPassword(CharSequence password)
    {
        if (password == null)
        {
            return false;
        }
        if (password.length() < 8 || password.length()>16 )
        {
            return false;
        }
        if ( !specailCharPatten.matcher(password).find())
        {
            return false;
        }
        if ( !UpperCasePatten.matcher(password).find() )
        {
            return false;
        }
        if (!lowerCasePatten.matcher(password).find())
        {
            return false;
        }
        if ( !digitCasePatten.matcher(password).find() )
        {
            return false;
        }
        return true;

    }


    @Override
    public void afterTextChanged(Editable editableText)
    {
        mIsValid = isValidPassword(editableText);
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
