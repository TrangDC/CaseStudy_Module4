package com.example.case_study_m4.model;

import java.util.HashMap;
import java.util.Map;

public class Cart {
    private Map<Game,Integer> games = new HashMap<>();

    public Cart() {
    }

    public Cart(Map<Game, Integer> products) {
        this.games = products;
    }

    public Map<Game, Integer> getGames() {
        return games;
    }


    //Kiểm tra sản phẩm đã có trong giỏ hàng chưa
    private boolean checkItemInCart(Game game) {
        for (Map.Entry<Game, Integer> entry: games.entrySet()) {
            if (entry.getKey().getId().equals(game.getId())) {
                return true;
            }
        }
        return false;
    }

    private Map.Entry<Game, Integer> selectItemInCart(Game game) {
        for (Map.Entry<Game, Integer> entry: games.entrySet()) {
            if (entry.getKey().getId().equals(game.getId())) {
                return entry;
            }
        }
        return null;
    }

    //Thêm sản phẩm vào giỏ hàng
    public void addProduct(Game game) {
        System.out.println("Before add: " + games);
        if (!checkItemInCart(game)) {
            games.put(game, 1);
        } else {
            Map.Entry<Game, Integer> itemEntry = selectItemInCart(game);
            assert itemEntry != null;
            Integer newQuantity = itemEntry.getValue() + 1;
            games.replace(itemEntry.getKey(), newQuantity);
            System.out.println("After add: " + games);
        }
    }

    //Giảm số lượng của 1 sản phẩm
    public void subProduct(Game game) {
        if (checkItemInCart(game)) {
            Map.Entry<Game,Integer> itemEntry = selectItemInCart(game);
            assert itemEntry != null;
            Integer currentQuantity = itemEntry.getValue();
            if (currentQuantity > 1) {
                Integer newQuantity = itemEntry.getValue() - 1;
                games.replace(itemEntry.getKey(), newQuantity);
            } else {
                games.remove(itemEntry.getKey());
            }
        }

    }

    //Đếm số lượng sản phẩm trong giỏ hàng
    public Integer countProductQuantity() {
        Integer productQuantity = 0;
        for (Map.Entry<Game, Integer> entry: games.entrySet()) {
            productQuantity += entry.getValue();
        }
        return productQuantity;
    };

    //Đếm tổng số lượng sản phẩm
    public Integer countItemQuantity() {
        return games.size();
    };

    //Tính tổng tiền cần thanh toán của giỏ hàng
    public Float countTotalPayment() {
        float payment = 0;
        for (Map.Entry<Game, Integer> entry: games.entrySet()) {
            payment += (float) (entry.getKey().getPrice() * (float) entry.getValue());
        }
        return payment;
    }

    public void deleteProductFromCart(Long id){
        for (Map.Entry<Game, Integer> entry: games.entrySet()) {
            if (entry.getKey().getId().equals(id)) {
                games.remove(entry.getKey());
                break;
            }
        }
    }

    public void deleteAllFromCart() {
        games.clear();
    }
}
