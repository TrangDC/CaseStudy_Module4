package com.example.case_study_m4.controller;

import com.example.case_study_m4.model.*;
import com.example.case_study_m4.service.IGameService;
import com.example.case_study_m4.service.IOrderDetailService;
import com.example.case_study_m4.service.IOrderService;
import com.example.case_study_m4.service.IUserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.sql.Timestamp;
import java.util.Map;
import java.util.Optional;

@Controller
@SessionAttributes("cart")
@RequestMapping("/checkout")
public class OrderController {

    @ModelAttribute("cart")
    public Cart setUpCart() {
        return new Cart();
    }

    @Autowired
    private IGameService iGameService;

    @Autowired
    private IOrderService iorderService;

    @Autowired
    private IOrderDetailService iOrderDetailService;

    @Autowired
    private IUserService iUserService;

    @GetMapping("")
    public String showOrder(HttpSession session, RedirectAttributes redirectAttributes) {
        Cart cart = (Cart) session.getAttribute("cart");

        // Chuyển dữ liệu giỏ hàng sang trang order
        redirectAttributes.addFlashAttribute("cart", cart);

        return "redirect:/checkout/order-summary";
    }

    @GetMapping("/order-summary")
    public ModelAndView showOrderSummary(@ModelAttribute("cart") Cart cart) {
        ModelAndView modelAndView;


        //Kiểm tra số lượng sản phẩm trong shop trước khi check đơn
        // Duyệt qua danh sách sản phẩm trong giỏ hàng
        ModelAndView modelAndView1 = checkQuantityFromShopData(cart);
        if (modelAndView1 != null) return modelAndView1;

        modelAndView = new ModelAndView("website/order_payment/order");
        modelAndView.addObject("cart", cart);
        return modelAndView;
    }

    private ModelAndView checkQuantityFromShopData(Cart cart) {
        ModelAndView modelAndView;
        for (Map.Entry<Game, Integer> entry : cart.getGames().entrySet()) {
            Game game = entry.getKey();
            Integer cartQuantity = entry.getValue();

            // lấy game từ CSDL để cập nhật dữ liệu
            Optional<Game> actualGameOptional = iGameService.findById(game.getId());
            if (actualGameOptional.isPresent()) {
                Game actualGame = actualGameOptional.get();

                // So sánh số lượng trong giỏ hàng với số lượng có sẵn trong cửa hàng
                if (cartQuantity > actualGame.getQuantity()) {
                    System.out.println(cartQuantity);
                    System.out.println(actualGame.getQuantity());
                    // Nếu số lượng vượt quá, trả về trang cart và hiển thị thông báo lỗi
                    modelAndView = new ModelAndView("website/shopping_cart/list");
                    modelAndView.addObject("error", "Game '" + game.getName() + "' mà bạn đặt vượt quá số lượng có sẵn trong cửa hàng.");
                    return modelAndView;
                }
            }
        }
        return null;
    }

    @PostMapping("/place-order")
    public ModelAndView payment(@ModelAttribute("cart") Cart cart,
                                @RequestParam("phoneNumber") String phoneNumber,
                                @RequestParam("shippingAddress") String shippingAddress,
                                @RequestParam("paymentMethod") String paymentMethod) {

        ModelAndView modelAndView = new ModelAndView();

        //giả sử một user mặc định. User sẽ lấy qua userid từ session
        Optional<User> user = iUserService.findById(Long.valueOf(3));

        if (user.isPresent()) {

            //Kiểm tra lần cuối số lượng game trong shop trước khi thanh toán
            // Duyệt qua danh sách sản phẩm trong giỏ hàng
            ModelAndView modelAndView1 = checkQuantityFromShopData(cart);
            if (modelAndView1 != null) return modelAndView1;

            //Tạo đơn hàng cho user
            Order order = new Order();

            order.setUser(user.get());
            order.setPhoneNumber(phoneNumber);
            order.setDeliveryAddress(shippingAddress);
            order.setOrderDate(new Timestamp(System.currentTimeMillis()));
            order.setStatus("Started");
            order.setTotalPrice(cart.countTotalPayment());
            order.setPayment_method(paymentMethod);

            // trừ tiền trong tài khoản nếu chọn thanh toán bằng account
            ModelAndView errorModelAndView = check_balance_if_user_choose_account_payment(paymentMethod, user, order);
            if (errorModelAndView != null) return errorModelAndView;

            iorderService.save(order);

            // Lấy danh sách products từ cart, cho vào orderdetail
            Map<Game, Integer> games = cart.getGames();
            for (Map.Entry<Game, Integer> entry : games.entrySet()) {
                Game game = entry.getKey();
                Long quantity = Long.valueOf(entry.getValue());

                OrderDetail orderDetail = new OrderDetail();
                orderDetail.setOrder(order);
                orderDetail.setGame(game);
                orderDetail.setQuantity(quantity);
                orderDetail.setPrice((double) (game.getPrice() * quantity));
                iOrderDetailService.save(orderDetail);

                // trừ đi số lượng sản phẩm
                game.setQuantity(game.getQuantity() - quantity);
                iGameService.save(game);
            }
            //xóa giỏ hàng
            cart.deleteAllFromCart();
            modelAndView = new ModelAndView("website/order_payment/success_payment");
            return modelAndView;
        }
        return new ModelAndView("/error_404");
    }

    private ModelAndView check_balance_if_user_choose_account_payment(String paymentMethod, Optional<User> user, Order order) {
        if (paymentMethod.equals("Account")) {
            // Kiểm tra xem balance có đủ để thanh toán không
            if (user.get().getBalance() >= order.getTotalPrice()) {
                // Trừ tiền từ balance và cập nhật thông tin đơn hàng
                user.get().setBalance((long) (user.get().getBalance() - order.getTotalPrice()));
                iUserService.save(user.get());
            } else {
                // Nếu balance không đủ, có thể xử lý theo ý bạn, ví dụ, hiển thị thông báo lỗi.
                ModelAndView errorModelAndView = new ModelAndView("website/order_payment/order");
                errorModelAndView.addObject("message", "Tài khoản của bạn không đủ để thanh toán.");
                return errorModelAndView;
            }
        }
        return null;
    }
}
