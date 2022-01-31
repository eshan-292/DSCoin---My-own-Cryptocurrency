package DSCoinPackage;

import java.util.*;
import HelperClasses.*;
public class Members
 {

  public String UID;
  public List<Pair<String, TransactionBlock>> mycoins;
  public Transaction[] in_process_trans;
 
  public void initiateCoinsend(String destUID, DSCoin_Honest DSobj) {
    Transaction tobj=new Transaction();
    Pair<String, TransactionBlock> send = mycoins.remove(0);
    tobj.Source=this;
    tobj.coinID=send.first;
    tobj.coinsrc_block=send.second;
    for(Members i:DSobj.memberlist){
      if(i.UID.equals(destUID)){
        tobj.Destination=i;
        break;
      }
    }
    if(this.in_process_trans==null){
			this.in_process_trans = new Transaction[1000];
		}
    for(int i=0;i<in_process_trans.length;i++){
      if(in_process_trans[i]==null){
        in_process_trans[i]=tobj;
      }
    }
    DSobj.pendingTransactions.AddTransactions(tobj);

  }
  public void initiateCoinsend(String destUID, DSCoin_Malicious DSobj) {
    Pair<String, TransactionBlock> send = mycoins.remove(0);
    Transaction tobj = new Transaction();
    tobj.Source=this;
    tobj.coinID=send.first;
    tobj.coinsrc_block=send.second;
    for(Members i:DSobj.memberlist){
      if(i.UID.equals(destUID)){
        tobj.Destination=i;
        break;
      }
    }
    if(in_process_trans==null){
      in_process_trans=new Transaction[1000];
    }
    for(int i=0;i<in_process_trans.length;i++){
			if(this.in_process_trans[i]==null){
				this.in_process_trans[i]=tobj;
				break;
			}
		}
    DSobj.pendingTransactions.AddTransactions(tobj);

  }
  public void pair_sort(List<Pair<String,TransactionBlock>> coin_list){
    Collections.sort(coin_list, new Comparator<Pair<String,TransactionBlock>>() {
        @Override
        public int compare(final Pair<String,TransactionBlock> qlll, final Pair<String,TransactionBlock> wtkk) {
            if (Integer.parseInt(qlll.first)==Integer.parseInt(wtkk.first)){return 0;}
            else if (Integer.parseInt(qlll.first)>Integer.parseInt(wtkk.first)){return 1;}
            else{return -1;}
        }
    });
  }


  public Pair<List<Pair<String, String>>, List<Pair<String, String>>> finalizeCoinsend (Transaction tobj, DSCoin_Honest DSObj) throws MissingTransactionException {
    TransactionBlock curr=DSObj.bChain.lastBlock;
    int x=(curr.trarray.length)/2,temp=0;
    boolean flag=true;
    while(curr!=null){
      for(int i=0;i<curr.trarray.length;i++){
        if(tobj==curr.trarray[i]){
          flag=false;
          temp=i+1;
          break;
        }
      }
      if(flag==false){
        break;
      }
      curr=curr.previous;
    }
    if(flag){
      throw new MissingTransactionException();
    }
    TreeNode curr_node=curr.Tree.rootnode;
    List<Pair<String, String>> path=new ArrayList<Pair<String, String>>();
    while(x>=1){
      if(temp<=x){
        curr_node=curr_node.left;
        x=x/2;
      }
      else{
        curr_node=curr_node.right;
        temp=temp-x;
        x=x/2;
      }
    } 
    while(curr_node.parent!=null){
      path.add(new Pair<String, String>(curr_node.parent.left.val,curr_node.parent.right.val));
      curr_node=curr_node.parent;
    }
    path.add(new Pair<String, String>(curr_node.val,null));
    TransactionBlock last=DSObj.bChain.lastBlock;
    ArrayList<Pair<String, String>> lish=new ArrayList<Pair<String, String>>();
    while(curr.previous!=last){
      Pair<String,String> op=new Pair<String,String>(last.dgst,last.previous.dgst+"#"+last.trsummary+"#"+last.nonce);
      lish.add(0,op);
      last=last.previous;
    }
    Pair<String,String> op=new Pair<String,String>(last.dgst,null);
    lish.add(0,op);
   
  Pair<List<Pair<String, String>>, List<Pair<String, String>>> ans= new Pair<List<Pair<String, String>>, List<Pair<String, String>>>(path,lish);
  Transaction[] nt=new Transaction[in_process_trans.length];
  int it=0;
  for(int i=0;i<in_process_trans.length;i++){
    if(tobj==in_process_trans[i]){
      it=-1;
    }
    else{nt[i+it]=in_process_trans[i];}
  }
  in_process_trans=nt;
  Pair<String, TransactionBlock> coin = new Pair<String, TransactionBlock>(tobj.coinID, curr);
   tobj.Destination.mycoins.add(coin);
   tobj.Destination.pair_sort(tobj.Destination.mycoins);
  return ans;
  }

  public void MineCoin(DSCoin_Honest DSObj) {
    int len=DSObj.bChain.tr_count-1;
    HashMap<String,String> map=new HashMap<String, String>(); 
    Transaction mr=new Transaction();
    Transaction[] arr=new Transaction[len+1];
    mr.coinID=String.valueOf(Integer.parseInt(DSObj.latestCoinID)+1);
    DSObj.latestCoinID=mr.coinID;
    mr.Destination=this;
    while(len>0){
      try {
        Transaction tk=DSObj.pendingTransactions.RemoveTransaction();
        if(DSObj.bChain.lastBlock.checkTransaction(tk) && map.get(tk.coinID)==null){
          map.put(tk.coinID,"dummy");
          arr[DSObj.bChain.tr_count-len-1]=tk;
          len--;
        } 
      } catch (Exception EmptyQueueException) {}
    }
    arr[DSObj.bChain.tr_count-1]=mr;
    TransactionBlock tb=new TransactionBlock(arr);
    mycoins.add(new Pair<String, TransactionBlock>(mr.coinID,tb));
    DSObj.bChain.InsertBlock_Honest(tb);
    this.pair_sort(mycoins);

  }
  public void MineCoin(DSCoin_Malicious DSObj){
    int len=DSObj.bChain.tr_count-1;
    HashMap<String,String> map=new HashMap<String, String>(); 
    Transaction mr=new Transaction();
    TransactionBlock la=DSObj.bChain.FindLongestValidChain();
    Transaction[] arr=new Transaction[len+1];
    mr.coinID=String.valueOf(Integer.parseInt(DSObj.latestCoinID)+1);
    DSObj.latestCoinID=mr.coinID;
    mr.Destination=this;
    while(len>0){
      try {
        Transaction tk=DSObj.pendingTransactions.RemoveTransaction();
        if(map.get(tk.coinID)==null && la.checkTransaction(tk)){
          map.put(tk.coinID,"dummy");
          arr[DSObj.bChain.tr_count-len-1]=tk;
          len--;
        }
      } catch (Exception EmptyQueueException){}
    }
    arr[DSObj.bChain.tr_count-1]=mr;
    TransactionBlock tb=new TransactionBlock(arr);
    mycoins.add(new Pair<String, TransactionBlock>(mr.coinID,tb));
    DSObj.bChain.InsertBlock_Malicious(tb);
    this.pair_sort(mycoins);
  }  

  
  
}
