package com.example.walkinclinics;


import android.text.Editable;
import android.text.TextWatcher;
import java.util.regex.Pattern;


public class ValidatorClinicName implements TextWatcher
{
    private boolean mIsValid = false;
    public boolean isValid() {
        return mIsValid;
    }

    public static boolean isValidClinicname(CharSequence clinicname)
    {
        Character ch;
        for (int i = 0; i < clinicname.length(); i++){
            ch = clinicname.charAt(i);
            if (!Character.isLetter(ch))
                if ((!ch.equals(' ')) && (!ch.equals('\'')))
                {
                    return false;
                }
        }
        if (clinicname == null)
        {
            return false;
        }
        return true;
    }

    @Override
    public void afterTextChanged(Editable editableText)
    {
        mIsValid = isValidClinicname(editableText);
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
