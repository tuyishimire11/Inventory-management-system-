package com.consessa.inventory_management.controller;

import com.consessa.inventory_management.model.Asset;
import com.consessa.inventory_management.model.AssetStatus;
import com.consessa.inventory_management.service.AssetService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class AssetController {

    private final AssetService assetService;

    public AssetController(AssetService assetService) {
        this.assetService = assetService;
    }

    @GetMapping("/")
    public String home() {
        return "index";
    }
    
    @GetMapping("/assets")
    public String listAssets(
            @RequestParam(required = false) String deviceType,
            @RequestParam(required = false) String department,
            @RequestParam(required = false) String condition,
            @RequestParam(required = false) String status,
            Model model) {
        AssetStatus assetStatus = null;
        if (status != null && !status.isEmpty()) {
            try {
                assetStatus = AssetStatus.valueOf(status);
            } catch (IllegalArgumentException e) {
                assetStatus = null;
            }
        }
        model.addAttribute("assets", assetService.searchAssets(deviceType, department, condition, assetStatus));
        return "assets";
    }

    @GetMapping("/asset/new")
    public String showAddForm(Model model) {
        model.addAttribute("asset", new Asset());
        return "new_asset";
    }

    @PostMapping("/asset/save")
    public String saveAsset(@ModelAttribute Asset asset) {
        asset.setIssueDate(null);
        asset.setReturnDate(null);
        assetService.saveAsset(asset);
        return "redirect:/assets";
    }

    @GetMapping("/asset/delete/{id}")
    public String deleteAsset(@PathVariable Long id) {
        assetService.deleteAsset(id);
        return "redirect:/assets";
    }

    @GetMapping("/asset/issue/{id}")
    public String showIssueForm(@PathVariable Long id, Model model) {
        Asset asset = assetService.getAssetById(id);
        model.addAttribute("asset", asset);
        return "issue-form";
    }

    @PostMapping("/asset/issue/{id}")
    public String issueAsset(@PathVariable Long id,
                             @RequestParam String ownerName,
                             @RequestParam String department) {
        assetService.issueAsset(id, ownerName, department);
        return "redirect:/assets";
    }

    @GetMapping("/asset/return/{id}")
    public String returnAsset(@PathVariable Long id) {
        assetService.returnAsset(id);
        return "redirect:/assets";
    }
}