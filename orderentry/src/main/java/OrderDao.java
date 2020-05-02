import com.secwager.proto.Market.Order;

public interface OrderDao {

  void insertOrder(Order order);

  void updateOrder(Order order);
}
