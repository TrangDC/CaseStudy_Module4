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
        ModelAndView modelAndView = new ModelAndView("website/order_payment/order");

        modelAndView.addObject("cart", cart);
        return modelAndView;
    }

    @PostMapping("/place-order")
    public ModelAndView payment(@ModelAttribute("cart") Cart cart,
                                @RequestParam("phoneNumber") String phoneNumber,
                                @RequestParam("shippingAddress") String shippingAddress,
                                @RequestParam("paymentMethod") String paymentMethod) {

        //giả sử một user mặc định. User sẽ lấy qua userid từ session
        Optional<User> user = iUserService.findById(Long.valueOf(3));

        System.out.println(user);
        if (user.isPresent()) {

            //Tạo đơn hàng cho user
            Order order = new Order();

            order.setUser(user.get());
            order.setPhoneNumber(phoneNumber);
            order.setDeliveryAddress(shippingAddress);
            order.setOrderDate(new Timestamp(System.currentTimeMillis()));
            order.setStatus("Started");
            order.setTotalPrice(cart.countTotalPayment());
            order.setPayment_method(paymentMethod);

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
            ModelAndView modelAndView = new ModelAndView("website/order_payment/success_payment");
            return modelAndView;
        }
        return new ModelAndView("/error_404");
    }
}
