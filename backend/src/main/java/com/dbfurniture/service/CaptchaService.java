package com.dbfurniture.service;

import com.dbfurniture.api.dto.CaptchaDTO;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.time.Instant;
import java.util.Base64;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class CaptchaService {

    private static final long CAPTCHA_TTL_SECONDS = 60; // 1min
    private static final int CAPTCHA_LEN = 4;
    private static final int IMAGE_W = 120;
    private static final int IMAGE_H = 40;

    private final ConcurrentHashMap<String, CaptchaEntry> captchaStore = new ConcurrentHashMap<>();
    private final Random random = new Random();

    public CaptchaService() {
        // 保证环境无 GUI 时也能生成图片
        System.setProperty("java.awt.headless", "true");
    }

    public CaptchaDTO newCaptcha() {
        String captchaId = UUID.randomUUID().toString();
        String code = randomDigits(CAPTCHA_LEN);

        BufferedImage image = renderCaptchaImage(code);
        String captchaImageBase64 = toBase64Png(image);

        CaptchaEntry entry = new CaptchaEntry();
        entry.code = code;
        entry.expireAt = Instant.now().plusSeconds(CAPTCHA_TTL_SECONDS);
        entry.used = false;
        captchaStore.put(captchaId, entry);

        CaptchaDTO dto = new CaptchaDTO();
        dto.captchaId = captchaId;
        dto.captchaImage = captchaImageBase64;
        return dto;
    }

    public CaptchaValidateResult validate(String captchaId, String captchaCode) {
        if (captchaId == null || captchaId.trim().isEmpty()) {
            return CaptchaValidateResult.invalid("验证码无效");
        }
        if (captchaCode == null) {
            return CaptchaValidateResult.invalid("验证码无效");
        }

        CaptchaEntry entry = captchaStore.get(captchaId);
        if (entry == null) {
            return CaptchaValidateResult.invalid("验证码无效");
        }

        if (Instant.now().isAfter(entry.expireAt)) {
            return CaptchaValidateResult.expired("验证码过期请重新获取并刷新图片");
        }
        if (entry.used) {
            return CaptchaValidateResult.invalid("验证码已使用");
        }

        String normalized = captchaCode.trim();
        if (!normalized.equals(entry.code)) {
            return CaptchaValidateResult.invalid("验证码错误");
        }

        entry.used = true;
        return CaptchaValidateResult.ok();
    }

    private String randomDigits(int len) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < len; i++) {
            sb.append(random.nextInt(10));
        }
        return sb.toString();
    }

    private BufferedImage renderCaptchaImage(String code) {
        BufferedImage image = new BufferedImage(IMAGE_W, IMAGE_H, BufferedImage.TYPE_INT_RGB);
        Graphics2D g = image.createGraphics();

        // 背景
        g.setColor(Color.WHITE);
        g.fillRect(0, 0, IMAGE_W, IMAGE_H);

        // 干扰线
        g.setColor(new Color(0xDDDDDD));
        for (int i = 0; i < 8; i++) {
            int x1 = random.nextInt(IMAGE_W);
            int y1 = random.nextInt(IMAGE_H);
            int x2 = random.nextInt(IMAGE_W);
            int y2 = random.nextInt(IMAGE_H);
            g.drawLine(x1, y1, x2, y2);
        }

        // 文本
        g.setColor(Color.BLACK);
        g.setFont(new Font("Arial", Font.BOLD, 22));
        FontMetrics fm = g.getFontMetrics();
        int textW = fm.stringWidth(code);
        int x = (IMAGE_W - textW) / 2;
        int y = (IMAGE_H + fm.getAscent() - fm.getDescent()) / 2;
        g.drawString(code, x, y);

        g.dispose();
        return image;
    }

    private String toBase64Png(BufferedImage image) {
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ImageIO.write(image, "png", baos);
            return Base64.getEncoder().encodeToString(baos.toByteArray());
        } catch (Exception e) {
            // MVP 兜底：异常时返回空字符串，前端展示报错即可
            return "";
        }
    }

    private static class CaptchaEntry {
        String code;
        Instant expireAt;
        boolean used;
    }

    public static class CaptchaValidateResult {
        public boolean ok;
        public String message;

        public static CaptchaValidateResult ok() {
            CaptchaValidateResult r = new CaptchaValidateResult();
            r.ok = true;
            r.message = "OK";
            return r;
        }

        public static CaptchaValidateResult expired(String msg) {
            CaptchaValidateResult r = new CaptchaValidateResult();
            r.ok = false;
            r.message = msg;
            return r;
        }

        public static CaptchaValidateResult invalid(String msg) {
            CaptchaValidateResult r = new CaptchaValidateResult();
            r.ok = false;
            r.message = msg;
            return r;
        }
    }
}

