package com.example.riskbattleprobs;


import androidx.annotation.NonNull;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class RiskProb {

    public static Map<Troops, Troops> lookup_map_expected = new HashMap<>();
    public static Map<Troops, Troops> lookup_map_prob = new HashMap<>();


    public Troops probOfWin;
    public Troops expectedVal;

    public static RiskProb calculateRiskProb(int numAttacking, int numDefending){
        RiskProb prob = new RiskProb();
        prob.probOfWin = calculateProbOfWin(numAttacking, numDefending);
        prob.expectedVal = calculateExpectedValue(numAttacking, numDefending);
        return prob;
    }

    public static Troops calculateExpectedValue(int attacking, int defending){
        Troops troops = new Troops(attacking, defending);
        if(attacking == 1 || defending == 0){
            return troops;
        }
        if(lookup_map_expected.containsKey(troops)){
            return lookup_map_expected.get(troops);
        }
        Troops val;
        if(troops.defending == 1){
            Troops t1 = calculateExpectedValue(attacking-1,defending);
            Troops t2 = calculateExpectedValue(attacking, defending-1);
            if(troops.attacking == 2) val = new Troops(t1.attacking*(7d/12)+t2.attacking*(5d/12),
                                                        t1.defending*(7d/12)+t2.defending*(5d/12));
            else if(troops.attacking == 3) val = new Troops(t1.attacking*(91d/216)+t2.attacking*(125d/216),
                                                            t1.defending*(91d/216)+t2.defending*(125d/216));
            else val = new Troops(t1.attacking*(441d/1296)+t2.attacking*(855d/1296),
                                    t1.defending*(441d/1296)+t2.defending*(855d/1296));
        }
        else{
            if(troops.attacking == 2){
                Troops t1 = calculateExpectedValue(attacking-1, defending);
                Troops t2 = calculateExpectedValue(attacking, defending-1);
                val = new Troops(t1.attacking*(161d/216)+t2.attacking*(55d/216),
                                t1.defending*(161d/216)+t2.defending*(55d/216));
            }
            else if(troops.attacking == 3){
                Troops t1 = calculateExpectedValue(attacking-2, defending);
                Troops t2 = calculateExpectedValue(attacking, defending-2);
                Troops t3 = calculateExpectedValue(attacking-1, defending-1);
                val = new Troops(t1.attacking*(571d/1296)+t2.attacking*(285d/1296)+t3.attacking*(440d/1296),
                                t1.defending*(571d/1296)+t2.defending*(285d/1296)+t3.defending*(440d/1296));
            }
            else{
                Troops t1 = calculateExpectedValue(attacking-2, defending);
                Troops t2 = calculateExpectedValue(attacking, defending-2);
                Troops t3 = calculateExpectedValue(attacking-1, defending-1);
                val = new Troops(t1.attacking*(2275d/7776)+t2.attacking*(2890d/7776)+t3.attacking*(2611d/7776),
                                t1.defending*(2275d/7776)+t2.defending*(2890d/7776)+t3.defending*(2611d/7776));
            }
        }
        lookup_map_expected.put(troops, val);
        return val;
    }

    public static Troops calculateProbOfWin(int attacking, int defending){
        if(defending == 0) return new Troops(1, 0);
        if(attacking == 1) return new Troops(0, 1);
        Troops troops = new Troops(attacking, defending);
        if(lookup_map_prob.containsKey(troops)){
            return lookup_map_prob.get(troops);
        }
        Troops val;
        if(troops.defending == 1){
            Troops t1 = calculateProbOfWin(attacking-1, defending);
            Troops t2 = calculateProbOfWin(attacking, defending-1);
            if(troops.attacking == 2) val = new Troops(t1.attacking*(7d/12)+t2.attacking*(5d/12),
                                                        t1.defending*(7d/12)+t2.defending*(5d/12));
            else if(troops.attacking == 3) val = new Troops(t1.attacking*(91d/216)+t2.attacking*(125d/216),
                                                            t1.defending*(91d/216)+t2.defending*(125d/216));
            else val = new Troops(t1.attacking*(441d/1296)+t2.attacking*(855d/1296),
                                    t1.defending*(441d/1296)+t2.defending*(855d/1296));
        }
        else {
            if(troops.attacking == 2){
                Troops t1 = calculateProbOfWin(attacking-1, defending);
                Troops t2 = calculateProbOfWin(attacking, defending-1);
                val = new Troops(t1.attacking*(161d/216)+t2.attacking*(55d/216),
                                t1.defending*(161d/216)+t2.defending*(55d/216));
            }
            else if(troops.attacking == 3){
                Troops t1 = calculateProbOfWin(attacking-2, defending);
                Troops t2 = calculateProbOfWin(attacking, defending-2);
                Troops t3 = calculateProbOfWin(attacking-1, defending-1);
                val = new Troops(t1.attacking*(571d/1296)+t2.attacking*(285d/1296)+t3.attacking*(440d/1296),
                                t1.defending*(571d/1296)+t2.defending*(285d/1296)+t3.defending*(440d/1296));
            }
            else {
                Troops t1 = calculateProbOfWin(attacking-2, defending);
                Troops t2 = calculateProbOfWin(attacking, defending-2);
                Troops t3 = calculateProbOfWin(attacking-1, defending-1);
                val = new Troops(t1.attacking*(2275d/7776)+t2.attacking*(2890d/7776)+t3.attacking*(2611d/7776),
                        t1.defending*(2275d/7776)+t2.defending*(2890d/7776)+t3.defending*(2611d/7776));
            }
        }
        lookup_map_prob.put(troops, val);
        return val;
    }

    public static class Troops {
        public double attacking, defending;
        public Troops(double attacking, double defending) {
            this.attacking = attacking;
            this.defending = defending;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Troops troops = (Troops) o;
            return troops.attacking == attacking && troops.defending == defending;
        }

        @Override
        public int hashCode() {
            return Objects.hash(attacking, defending);
        }

        @NonNull
        public String toString(){
            return attacking+", "+defending;
        }
    }

    @NonNull
    public String toString(){
        DecimalFormat df = new DecimalFormat("#.##");
        df.setRoundingMode(RoundingMode.HALF_UP);
        String string = "";
        if(probOfWin != null){
            string += "Chance of Attackers winning: "+df.format(probOfWin.attacking*100)+"%\n"+
                    "Chance of Defenders winning: "+df.format(probOfWin.defending*100)+"%\n";
        }
        if(expectedVal != null){
            string += "Expected Value of Attackers: "+df.format(expectedVal.attacking)+"\n"+
                    "Expected Value of Defenders: "+df.format(expectedVal.defending);
        }
        return string;
    }
}
