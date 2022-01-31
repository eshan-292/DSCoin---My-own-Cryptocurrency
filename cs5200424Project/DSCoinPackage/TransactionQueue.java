package DSCoinPackage;
import java.util.ArrayList;
public class TransactionQueue {

  public Transaction firstTransaction;
  public Transaction lastTransaction;
  public int numTransactions=0;

  public void AddTransactions (Transaction transaction) {
    if(firstTransaction==null){
      firstTransaction=transaction;
      lastTransaction=transaction;
      numTransactions=1;
    }
    else{
      lastTransaction.next=transaction;
      transaction.previous=lastTransaction;
      lastTransaction=transaction;
      numTransactions++;
    }
  }
  
  public Transaction RemoveTransaction () throws EmptyQueueException {
    Transaction t=firstTransaction;
    if(firstTransaction==null){
      throw new EmptyQueueException();
    }
    else if(numTransactions==1){
      firstTransaction=null;
      lastTransaction=null;
      numTransactions=0;
    }
    else{
      firstTransaction=firstTransaction.next;
      numTransactions-- ;
    }
    return t;
  }

  public int size() {
    return numTransactions;
  }
}
