package com.example.walkinclinics;

import android.text.Editable;
import android.text.TextWatcher;
import java.util.regex.Pattern;

public class ValidatorServiceName implements TextWatcher
{
    private boolean mIsValid = false;
    public boolean isValid() {
        return mIsValid;
    }

    public static final Pattern specailCharPatten = Pattern.compile("[^a-z0-9 ]", Pattern.CASE_INSENSITIVE);
    public static final Pattern digitCasePatten = Pattern.compile("[0-9 ]");


    public static boolean isValidServiceName(CharSequence servicename)
    {
        if (servicename == null)
        {
            return false;
        }
        if (specailCharPatten.matcher(servicename).find())
        {
            return false;
        }
        if (digitCasePatten.matcher(servicename).find())
        {
            return false;
        }
        return true;
    }

    @Override
    public void afterTextChanged(Editable editableText)
    {
        mIsValid = isValidServiceName(editableText);
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
