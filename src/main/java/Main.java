import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Random;
import java.util.concurrent.ForkJoinPool;

public class Main {
    public static void main(String[] args){
        ArrayList<ArrayList> list = new ArrayList<>();
        int j = 1;
        int k = 0;
        Random rnd = new Random();
        int tuples_num = Integer.parseInt(args[0]);
//        int tuples_num = 1000000;
        for(int i = 0; i < tuples_num; i++){
            ArrayList tmp = new ArrayList();
            int random = rnd.nextInt(10) + 1;
            if(i % 25 == 0 && i != 0){
                j++;
            }
            if(i % 5 == 0 && i != 0){
                k++;
            }
            if(i % 2 == 0){
                tmp.add("male");
                tmp.add("genre"+j);
                tmp.add("item"+k);
                tmp.add(random);
            }else{
                tmp.add("female");
                tmp.add("genre"+j);
                tmp.add("item"+k);
                tmp.add(random);
            }
            list.add(tmp);
        }
        System.out.println("Tuple size: "+tuples_num);
//        System.out.println("Tuples: "+ list);
        Hashtable result;
        double start= System.currentTimeMillis();
        //そのままやる場合
//        result = recursiveSort(list);
        //並列の場合
        ForkJoinPool fj = new ForkJoinPool();
        result = fj.invoke(new RecursiveSort(list));

        double end = System.currentTimeMillis();
        System.out.println("Time taken: " + (end - start) + "ms");
//        System.out.println();
//        System.out.println(result);
    }

    public static Hashtable recursiveSort(ArrayList listlist){
        boolean last_flag = false;
        Hashtable buf = new Hashtable();
        for(Object list: listlist){
            ArrayList alist = (ArrayList)list;
            ArrayList key = new ArrayList();
            ArrayList value = new ArrayList();
            key.add(alist.get(0).toString());
            ArrayList tmp = new ArrayList();
            for(int i = 1; i < alist.size(); i++){
                tmp.add(alist.get(i));
            }
            if(buf.containsKey(key)){
                value = (ArrayList)buf.get(key);
                value.add(tmp);
            }else{
                value.add(tmp);
            }
            buf.put(key, value);
            if(((ArrayList)value.get(0)).size() == 1){
                last_flag = true;
            }
        }
        if(!last_flag){
            Enumeration keys = buf.keys();
            while(keys.hasMoreElements()){
                Object key = keys.nextElement();
                ArrayList value_in = (ArrayList)buf.get(key);
                Hashtable ret = recursiveSort(value_in);
                buf.put(key, ret);
            }
            return buf;
        }else{
            return buf;
        }
    }
}
