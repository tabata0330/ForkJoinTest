import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.List;
import java.util.concurrent.RecursiveTask;

class RecursiveSort extends RecursiveTask<Hashtable> {
    private final ArrayList list;
    private final ArrayList key;

    public RecursiveSort(ArrayList<ArrayList> list) {
        this(list, new ArrayList());
    }

    public RecursiveSort(ArrayList<ArrayList> list, ArrayList key) {
        this.list = list;
        this.key = key;
    }

    @Override
    protected Hashtable compute() {
        if(((List)this.list.get(0)).size() == 1){
            Hashtable buf = new Hashtable();
            buf.put(this.key, this.list);
            return buf;
        }else{
            Hashtable buf = new Hashtable();
            for(Object l: list){
                ArrayList alist = (ArrayList)l;
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
            }
            Enumeration keys = buf.keys();
            List<RecursiveSort> fjList = new ArrayList<>();
            while(keys.hasMoreElements()){
                Object key = keys.nextElement();
                ArrayList value_in = (ArrayList)buf.get(key);
                ArrayList tmp = new ArrayList();
                tmp.add(key.toString());
                RecursiveSort rs = new RecursiveSort(value_in, tmp);
                rs.fork();
                fjList.add(rs);
            }
            Hashtable result = new Hashtable();
            ArrayList vList = new ArrayList();
            for(RecursiveSort r: fjList){
                vList.add(r.join());
            }
            result.put(this.key, vList);
            return result;
        }
    }
}
