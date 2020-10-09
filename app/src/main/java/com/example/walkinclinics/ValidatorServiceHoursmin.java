package com.example.walkinclinics;

import android.text.Editable;
import android.text.TextWatcher;
import java.util.regex.Pattern;

public class ValidatorServiceHoursmin implements TextWatcher
{
    private boolean mIsValid = false;
    public boolean isValid() {
        return mIsValid;
    }



    public static boolean isValidServiceHoursmin(CharSequence ServiceHourshourmin)
    {
        Integer integer=null;
        try{
            integer = Integer.parseInt(ServiceHourshourmin.toString());
        }
        catch (Exception e){}
        if(integer == null)
        {
            return false;
        }
        else if(integer<0 || integer>59)
        {
            return false;
        }
        else
            {
            return true;
        }
    }

    @Override
    public void afterTextChanged(Editable editableText)
    {
        mIsValid = isValidServiceHoursmin(editableText);
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

