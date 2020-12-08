package com.example.cst8334_glutentracker.functionality;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.example.cst8334_glutentracker.R;
import com.example.cst8334_glutentracker.activity.RegisterActivity;

public class ValidateMessageFragment extends Fragment {

    private TextView validateMessage;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View validateMessageView = inflater.inflate(R.layout.validate_message_fragment, container, false);
        validateMessage = validateMessageView.findViewById(R.id.validate_message);
        Bundle getMessages = getArguments();
        validateMessage.setText(getMessages.getString(RegisterActivity.KEY_ERROR_MESSAGE));
        return validateMessageView;
    }
}
