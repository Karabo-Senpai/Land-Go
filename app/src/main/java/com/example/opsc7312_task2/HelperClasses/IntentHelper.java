package com.example.opsc7312_task2.HelperClasses;

import android.content.Context;
import android.content.Intent;

//Helper Class To Help Menu Navigate Between Screens
public class IntentHelper {


public void Nav_Open_Intent(Context context , Class newActivity){

    Intent intent = new Intent(context,newActivity);
    intent.putExtra("New Activity",newActivity);
    context.startActivity(intent);


}

}
