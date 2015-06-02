/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package Threadtest;

import Graph.Stack;
import javafx.scene.web.WebEngine;

/**
 *
 * @author Rohan
 */
public class Test {

    double coList[][];
    int i =0;
    public Test() {
    
    coList = new double[][]{
    {42.32599290979131 , -71.1116439950517},
{42.44 , -71.11164400130363},
{42.66 , -71.33},
{42.33599290979131 , -71.1116440002685},
{42.32599290979131 , -71.11164399979008},
{42.33599290979131 , -71.1116440000553},
{42.32599290979131 , -71.11164399995675},
{42.33599290979131 , -71.1116440000114},
{42.32599290979131 , -71.11164399999109},
{42.33599290979131 , -71.11164400000234},
{42.32599290979131 , -71.11164399999817},
{42.33599290979131 , -71.11164400000048},
{42.32599290979131 , -71.11164399999961},
{42.33599290979131 , -71.1116440000001},
{42.32599290979131 , -71.11164399999993},
{42.33599290979131 , -71.11164400000001},
{42.32599290979131 , -71.11164399999998},
{42.33599290979131 , -71.111644},
{42.32599290979131 , -71.111644},
{42.32907406727151 , -71.09219033729244},
{42.330966459305785 , -71.10200964747129},
{42.33218987524781 , -71.11193452799615}      
    };
    }
    
    public synchronized void moveDrone(Stack s,WebEngine webEngine){
//        Link current=s.getBottom();
//        webEngine.executeScript("document.setMarkerInitialPositionAt("+current.getLatitude()+","+current.getLongitude()+")");
      
        while(i<9){
        webEngine.executeScript("document.setMarkerAt("+coList[i][0]+","+coList[i][1]+")"); i++;
        
              long sysTime = System.currentTimeMillis();
                final long end = sysTime + 1*1000;
                while(System.currentTimeMillis()<end){ 
                }
        
        System.out.println(i);
        }
}
}
