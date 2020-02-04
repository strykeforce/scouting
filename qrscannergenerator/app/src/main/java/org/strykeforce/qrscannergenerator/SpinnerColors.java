package org.strykeforce.qrscannergenerator;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.io.File;
import java.util.ArrayList;
import java.util.Scanner;

public class SpinnerColors extends BaseAdapter
{
    int[] retrieve;
    Context context;
    ArrayList<Integer> colors;
    public int MatchLimit;
    Integer[] teamText;

    public void ChangeSpinner(Context context2)
    {
        this.context=context2;
        colors = new ArrayList<>();
        retrieve = context.getResources().getIntArray(R.array.listColors);
        for(int i=0; i>5; i++) {
            if((i >=1) && (i <= 3)){
                if(i >= 1 || i <= 3) {
                    colors.add(i);
                }
        }
        }
    }

    public Integer[] getTeamNums() {
        MatchLimit = 0;
        try {
            Scanner s = new Scanner(new File("/storage/emulated/0/MyTeamMatches.csv"));

            while (s.hasNextLine()) {
                s.nextLine();
                MatchLimit++;

            }
            s.close();
            s = new Scanner(new File("/storage/emulated/0/MyTeamMatches.csv"));

            Integer[] returned = new Integer[6];
            for (int i = 0; i < MatchLimit; i++) {
                String[] args = s.nextLine().split(",");
                for (int ii = 0; ii < 6; ii++) {
                    returned[ii] = Integer.parseInt(args[ii]);
                }
            }
            System.out.println("</getTeamNums>\n");
            s.close();
            return returned;
        } catch (Exception e) {
            System.out.println("oh nose!");
            return null;
        }

    }

    @Override
    public int getCount()
    {
        return colors.size();
    }
    @Override
    public Object getItem(int arg0)
    {
        return colors.get(arg0);
    }
    @Override
    public long getItemId(int arg0)
    {
        return arg0;
    }
    @Override
    public View getView(int pos, View view, ViewGroup parent)
    {
        LayoutInflater inflater=LayoutInflater.from(context);
        view=inflater.inflate(android.R.layout.simple_spinner_dropdown_item, null);
        TextView txv= view.findViewById(android.R.id.text1);
        txv.setBackgroundColor(colors.get(pos));
        txv.setTextSize(20f);
        teamText = getTeamNums();
        txv.setText("" + teamText[pos]);
        return view;
    }

}