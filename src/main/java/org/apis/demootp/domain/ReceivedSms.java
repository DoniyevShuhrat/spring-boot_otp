package org.apis.demootp.domain;

import lombok.Data;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;
import org.hibernate.annotations.Parameter;
import org.apis.demootp.config.PostgreSQLEnumUserType;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;

@Data // Lombok getter, setter, toString .. kabilarni avtomatik yaratadi
@Entity // Bu Class database'dagi jadvalga mos ekanligini bildiradi
@Table(name = "received_sms") // Qaysi table'ga mosligini aniq ko'rsatadi
@TypeDef(
        name = "pgsql_enum",
        typeClass = org.apis.demootp.config.PostgreSQLEnumUserType.class  // To'liq package name
)
public class ReceivedSms {

    @Id // Bu field primary key ekanligini bildiradi
    @GeneratedValue(strategy = GenerationType.IDENTITY) // ID'ni auto oshiradi auto-increment
    private Long id;

    @Column(name = "phone_number", nullable = false)
    private String phoneNumber;

    @Column(name = "sms_message", columnDefinition = "TEXT", nullable = false)
    private String smsMessage;

    @Column(name = "otp_code")
    private String otpCode;

    @Column(name = "correlation_id")
    private UUID correlationId;

    @Column(name = "sender_id")
    private String senderId;

    // PROFESSIONAL YECHIM: Custom UserType bilan
    @Type(type = "pgsql_enum",
            parameters = @Parameter(name = "enumClassName", value = "org.apis.demootp.domain.SmsStatus"))
    @Column(name = "status", nullable = false, columnDefinition = "sms_status")
    private SmsStatus status;

    @Column(name = "sent_at")
    private LocalDateTime sentAt;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    @Column(name = "expires_at", nullable = false)
    private LocalDateTime expiresAt;

    @Column(name = "used_at")
    private LocalDateTime usedAt;
}
