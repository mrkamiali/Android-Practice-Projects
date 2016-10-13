package com.example.kamranali.expandableview;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by kamranali on 07/10/2016.
 */

public class Version_List {
    public static HashMap<String, List<Double>> getInfo() {
        HashMap<String, List<Double>> version_list = new HashMap<String, List<Double>>();
        List<Double> jellbean;
        List<Double> kitkat;
        List<Double> lolipop;
        List<Double> marshmallow;

        jellbean = new ArrayList<>();
        jellbean.add(4.0);
        jellbean.add(4.1);
        jellbean.add(4.2);

        kitkat = new ArrayList<>();
        kitkat.add(4.3);
        kitkat.add(4.4);
        kitkat.add(4.5);
        lolipop = new ArrayList<>();
        lolipop.add(5.0);
        lolipop.add(5.1);
        lolipop.add(5.2);
        marshmallow = new ArrayList<>();
        marshmallow.add(6.0);
        marshmallow.add(6.2);
        marshmallow.add(6.3);


        version_list.put("JellyBean", jellbean);
        version_list.put("KitKat", kitkat);
        version_list.put("Lolipop", lolipop);
        version_list.put("MarshMallow", marshmallow);

        return version_list;
    }
}
