package com.consessa.inventory_management.controller;

import com.consessa.inventory_management.model.Asset;
import com.consessa.inventory_management.model.AssetStatus;
import com.consessa.inventory_management.service.AssetService;
import com.consessa.inventory_management.service.AuditService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
public class ReportController {

    private final AssetService assetService;
    private final AuditService auditService;

    public ReportController(AssetService assetService, AuditService auditService) {
        this.assetService = assetService;
        this.auditService = auditService;
    }

    @GetMapping("/reports")
    public String showReports(Model model) {
        List<Asset> allAssets = assetService.getAllAssets();

        // Filter out null department keys
        Map<String, Long> byDepartment = allAssets.stream()
                .filter(a -> a.getDepartment() != null)
                .collect(Collectors.groupingBy(Asset::getDepartment, Collectors.counting()));

        // Filter out null deviceType keys
        Map<String, Long> byDeviceType = allAssets.stream()
                .filter(a -> a.getDeviceType() != null)
                .collect(Collectors.groupingBy(Asset::getDeviceType, Collectors.counting()));

        // Filter out null condition keys
        Map<String, Long> byCondition = allAssets.stream()
                .filter(a -> a.getCondition() != null)
                .collect(Collectors.groupingBy(Asset::getCondition, Collectors.counting()));

        // Filter out null status keys (and also ensure status is not null, default to AVAILABLE if needed)
        Map<AssetStatus, Long> byStatus = allAssets.stream()
                .filter(a -> a.getStatus() != null)
                .collect(Collectors.groupingBy(Asset::getStatus, Collectors.counting()));

        List<Asset> activeAssignments = allAssets.stream()
                .filter(a -> a.getIssueDate() != null && a.getReturnDate() == null)
                .collect(Collectors.toList());

        model.addAttribute("byDepartment", byDepartment);
        model.addAttribute("byDeviceType", byDeviceType);
        model.addAttribute("byCondition", byCondition);
        model.addAttribute("byStatus", byStatus);
        model.addAttribute("activeAssignments", activeAssignments);
        model.addAttribute("totalAssets", allAssets.size());

        return "reports";
    }

    @GetMapping("/reports/date")
    public String dateRangeReport(@RequestParam(required = false) String startDate,
                                  @RequestParam(required = false) String endDate,
                                  Model model) {
        List<Asset> allAssets = assetService.getAllAssets();
        LocalDate start = (startDate != null && !startDate.isEmpty()) ? LocalDate.parse(startDate) : null;
        LocalDate end = (endDate != null && !endDate.isEmpty()) ? LocalDate.parse(endDate) : null;

        List<Asset> filteredAssets = allAssets.stream()
                .filter(a -> {
                    if (start != null && a.getIssueDate() != null && a.getIssueDate().isBefore(start))
                        return false;
                    if (end != null && a.getIssueDate() != null && a.getIssueDate().isAfter(end))
                        return false;
                    return true;
                })
                .collect(Collectors.toList());

        model.addAttribute("dateFilteredAssets", filteredAssets);
        model.addAttribute("startDate", startDate);
        model.addAttribute("endDate", endDate);
        return "date-report";
    }

    @GetMapping("/audit")
    public String showAuditLog(Model model) {
        model.addAttribute("logs", auditService.getAllLogs());
        return "audit";
    }
}