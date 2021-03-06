/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.thinkgem.jeesite.modules.manager.orders.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.thinkgem.jeesite.api.entity.res.PlatformRes;
import com.thinkgem.jeesite.api.service.OrderService;
import com.thinkgem.jeesite.modules.manager.ordergoods.entity.OrderGoods;
import com.thinkgem.jeesite.modules.manager.ordergoods.service.OrderGoodsService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.thinkgem.jeesite.common.config.Global;
import com.thinkgem.jeesite.common.persistence.Page;
import com.thinkgem.jeesite.common.web.BaseController;
import com.thinkgem.jeesite.common.utils.StringUtils;
import com.thinkgem.jeesite.modules.manager.orders.entity.Orders;
import com.thinkgem.jeesite.modules.manager.orders.service.OrdersService;

import java.util.List;

/**
 * 订单实体类Controller
 *
 * @author yt
 * @version 2017-08-19
 */
@Controller
@RequestMapping(value = "${adminPath}/orders/orders")
public class OrdersController extends BaseController {

    @Autowired
    private OrdersService ordersService;
    @Autowired
    private OrderService orderService;
    @Autowired
    private OrderGoodsService orderGoodsService;

    @ModelAttribute
    public Orders get(@RequestParam(required = false) String id) {
        Orders entity = null;
        if (StringUtils.isNotBlank(id)) {
            entity = ordersService.get(id);
        }
        if (entity == null) {
            entity = new Orders();
        }
        return entity;
    }

    @RequiresPermissions("orders:orders:view")
    @RequestMapping(value = {"list", ""})
    public String list(Orders orders, HttpServletRequest request, HttpServletResponse response, Model model) {
        if (orders.getOrderStatus() == null) {
            orders.setOrderStatus(1);
        }

        Page<Orders> page = ordersService.findPage(new Page<Orders>(request, response), orders);
        model.addAttribute("page", page);
        return "manager/orders/ordersList";
    }

    @RequiresPermissions("orders:orders:view")
    @RequestMapping(value = "form")
    public String form(Orders orders, Model model) {
        List<OrderGoods> orderGoods = orderGoodsService.getOrderGoodsByOrderNo(orders.getOrderNo());
        model.addAttribute("orders", orders);
        model.addAttribute("orderGoods", orderGoods);
        return "manager/orders/ordersForm";
    }

    @RequiresPermissions("orders:orders:edit")
    @RequestMapping(value = "save")
    public String save(Orders orders, Model model, RedirectAttributes redirectAttributes) {
        if (!beanValidator(model, orders)) {
            return form(orders, model);
        }
        ordersService.save(orders);
        addMessage(redirectAttributes, "保存订单管理成功");
        return "redirect:" + Global.getAdminPath() + "/orders/orders/?repage";
    }

    @RequiresPermissions("orders:orders:edit")
    @RequestMapping(value = "delete")
    public String delete(Orders orders, RedirectAttributes redirectAttributes) {
        ordersService.delete(orders);
        addMessage(redirectAttributes, "删除订单管理成功");
        return "redirect:" + Global.getAdminPath() + "/orders/orders/?repage";
    }


    @RequiresPermissions("orders:orders:refund")
    @RequestMapping(value = "refund")
    public String refund(Orders orders, HttpServletRequest request, HttpServletResponse response, Model model) {
        //状态为已支付,或者已取餐的
        orders.setPayOrderStatus("1,3");
        //退款的
        orders.setRefundOrderStatus("is not null");

        Page<Orders> page = ordersService.findPage(new Page<Orders>(request, response), orders);
        model.addAttribute("page", page);
        return "manager/orders/ordersRefundList";
    }


    @RequiresPermissions("orders:orders:refund")
    @RequestMapping(value = "queryorder")
    public String queryOrderList(String orderNo, Integer queryType, Model model) {
        if (StringUtils.isNotBlank(orderNo)) {
            model.addAttribute("orderNo", orderNo);
        }

        if (queryType != null)
            model.addAttribute("queryType", queryType);

        if (queryType != null) {
            if (queryType == 0) {
                PlatformRes<Orders> orders = orderService.queryOrder(orderNo);
                model.addAttribute("order", orders);
            } else if (queryType == 1) {
                PlatformRes<String> orders = orderService.refundOrder(orderNo);
                model.addAttribute("order", orders);
            } else if (queryType == 2) {
                PlatformRes<String> orders = orderService.queryRefundOrder(orderNo);
                model.addAttribute("order", orders);
            }
        }
        return "manager/orders/queryorder";
    }

}