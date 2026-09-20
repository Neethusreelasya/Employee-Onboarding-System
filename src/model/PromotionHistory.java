package model;

public class PromotionHistory {
    private String previousDesignation;
    private String newDesignation;
    private String promotionDate;

    public String getPreviousDesignation() { return previousDesignation; }
    public void setPreviousDesignation(String previousDesignation) { this.previousDesignation = previousDesignation; }
    public String getNewDesignation() { return newDesignation; }
    public void setNewDesignation(String newDesignation) { this.newDesignation = newDesignation; }
    public String getPromotionDate() { return promotionDate; }
    public void setPromotionDate(String promotionDate) { this.promotionDate = promotionDate; }
}
