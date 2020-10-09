package com.example.walkinclinics;

import android.text.Editable;
import android.text.TextWatcher;
import java.util.regex.Pattern;

public class ValidatorServiceHourshour implements TextWatcher
{
    private boolean mIsValid = false;
    public boolean isValid() {
        return mIsValid;
    }

//    public static final Pattern specailCharPatten = Pattern.compile("[^a-z0-9 ]", Pattern.CASE_INSENSITIVE);
//    public static final Pattern UpperCasePatten = Pattern.compile("[A-Z ]");
//    public static final Pattern lowerCasePatten = Pattern.compile("[a-z ]");
//    public static final Pattern digitCasePatten = Pattern.compile("[0-9 ]");



    public static boolean isValidServiceHourshour(CharSequence ServiceHourshour)
    {
//        if (ServiceHourshour.length() > 2)
//        {
//            return false;
//        }
//        if (ServiceHourshour == null)
//        {
//            return false;
//        }
//        if (specailCharPatten.matcher(ServiceHourshour).find())
//        {
//            return false;
//        }
//        if (UpperCasePatten.matcher(ServiceHourshour).find())
//        {
//            return false;
//        }
//        if (lowerCasePatten.matcher(ServiceHourshour).find())
//        {
//            return false;
//        }
//        if (!digitCasePatten.matcher(ServiceHourshour).find())
//        {
//            return false;
//        }
//        return true;
        Integer integer=null;
        try{
            integer = Integer.parseInt(ServiceHourshour.toString());
        }
        catch (Exception e){}
        if(integer == null)
        {
            return false;
        }
        else if(integer<6 || integer>23)
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
        mIsValid = isValidServiceHourshour(editableText);
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
