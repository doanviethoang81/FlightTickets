package com.example.banvemaybay.dtos;


public class UserRoleAdminDTO {
    private int id;
    private String ho;
    private String soDienThoai;
    private String email;
    private boolean enable;
    private String provider;

    public UserRoleAdminDTO(int id, String ho, String soDienThoai, String email, boolean enable, String provider) {
        this.id = id;
        this.ho = ho;
        this.soDienThoai = soDienThoai;
        this.email = email;
        this.enable = enable;
        this.provider = provider;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getHo() {
        return ho;
    }

    public void setHo(String ho) {
        this.ho = ho;
    }

    public String getSoDienThoai() {
        return soDienThoai;
    }

    public void setSoDienThoai(String soDienThoai) {
        this.soDienThoai = soDienThoai;
    }

    public boolean isEnable() {
        return enable;
    }

    public void setEnable(boolean enable) {
        this.enable = enable;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getProvider() {
        return provider;
    }

    public void setProvider(String provider) {
        this.provider = provider;
    }
}
