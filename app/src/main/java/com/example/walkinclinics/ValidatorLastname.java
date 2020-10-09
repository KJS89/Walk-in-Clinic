package com.example.walkinclinics;

import android.text.Editable;
import android.text.TextWatcher;
import java.util.regex.Pattern;

public class ValidatorLastname implements TextWatcher
{
    private boolean mIsValid = false;
    public boolean isValid() {
        return mIsValid;
    }

    public static final Pattern specailCharPatten = Pattern.compile("[^a-z0-9 ]", Pattern.CASE_INSENSITIVE);
    public static final Pattern UpperCasePatten = Pattern.compile("[A-Z ]");
    public static final Pattern lowerCasePatten = Pattern.compile("[a-z ]");
    public static final Pattern digitCasePatten = Pattern.compile("[0-9 ]");



    public static boolean isValidLastname(CharSequence lastname)
    {
        if (lastname.length() > 10)
        {
            return false;
        }
        if (lastname == null)
        {
            return false;
        }
        if (specailCharPatten.matcher(lastname).find())
        {
            return false;
        }
        if (!UpperCasePatten.matcher(lastname).find())
        {
            return false;
        }
        if (!lowerCasePatten.matcher(lastname).find())
        {
            return false;
        }
        if (digitCasePatten.matcher(lastname).find())
        {
            return false;
        }
        return true;
    }

    @Override
    public void afterTextChanged(Editable editableText)
    {
        mIsValid = isValidLastname(editableText);
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
