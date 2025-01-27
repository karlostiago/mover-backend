package com.ctsousa.mover.enumeration;

import com.ctsousa.mover.core.exception.notification.NotificationException;
import lombok.Getter;

@Getter
public enum BankIcon {
    BANK_BB(1,"","Banco Do Brasil", "assets/icons/bb.png"),
    BANK_CAIXA(2,"","Caixa Econômica", "assets/icons/caixa.png"),
    BANK_BRADESCO(3,"","Bradesco", "assets/icons/bradesco.png"),
    BANK_ITAU(4,"","Itaú", "assets/icons/itau.png"),
    BANK_SANTANDER(5,"","Santander", "assets/icons/santander.png"),
    BANK_SAFRA(6,"Banco","Safra","assets/icons/bancosafra.png"),
    BANK_BANRISUL(7,"","Banrisul","assets/icons/banrisul.png"),
    BANK_BNB(8,"Banco Do","Nordeste", "assets/icons/bnb.png"),
    BANK_BASA(9,"Banco","Basa", "assets/icons/basa.png"),
    BANK_VOTORANTIM(10,"Banco","Votorantim", "assets/icons/bv.png"),
    BANK_INTER(11,"Banco","Inter", "assets/icons/bancointer.png"),
    BANK_ORIGINAL(12,"Banco","Original","assets/icons/original.png"),
    BANK_BTG_PACTUAL(13,"Banco","BTG Pactual", "assets/icons/btgpactual.png"),
    BANK_DAYCOVAL(14,"Banco","Daycoval", "assets/icons/daycoval.png"),
    BANK_PAN(15,"Banco","Pan", "assets/icons/bancopan.png"),
    BANK_SICREDI(16,"Banco","Sicredi", "assets/icons/sicredi.png"),
    BANK_SICOOB(17,"Banco", "Sicoob", "assets/icons/sicoob.png"),
    BANK_BMG(18,"Banco","BMG", "assets/icons/bancobmg.png"),
    BANK_AGIBANK(19,"","AGI Bank", "assets/icons/agibank.png"),
    BANK_NUBANK(20,"","Nubank", "assets/icons/nubank.png"),
    BANK_CORA(21,"Banco","Cora", "assets/icons/cora.png"),
    BANK_C6(22,"","C6 Bank", "assets/icons/c6bank.png"),
    BANK_PAGBANK(23,"","Pagbank", "assets/icons/pagbank.png"),
    BANK_NEON(24,"Banco", "Neon", "assets/icons/neon.png"),
    BANK_NEXT(25,"Banco", "Next", "assets/icons/next.png"),
    BANK_BS2(26,"Banco", "B2B", "assets/icons/bs2.png"),
    BANK_SOFISA(27,"Banco", "Sofisa", "assets/icons/sofisa.png"),
    BANK_PICPAY(28,"","Picpay", "assets/icons/picpay.png"),
    BANK_DIGIO(29,"","Digio", "assets/icons/digio.png"),
    BANK_WILL(30,"","Will Bank", "assets/icons/bancowill.png"),
    BANK_MERCADO_PAGO(31,"","Mercado Pago", "assets/icons/mercadopago.png"),
    BANK_BANQI(32,"Banco","BanQi", "assets/icons/banqi.png"),
    BANK_BARI(33,"Banco", "Bari", "assets/icons/bari.png"),
    BANK_GENERIC(34,"Banco","Genérico ", "assets/icons/bit.png");

    private final Integer code;
    private final String prefix;
    private final String bankName;
    private final String urlImage;

    BankIcon(Integer code, String prefix, String bankName, String urlImage) {
        this.prefix = prefix;
        this.code = code;
        this.bankName = bankName;
        this.urlImage = urlImage;
    }

    public static BankIcon toCode(Integer code) {
        for (BankIcon bankIcon : BankIcon.values()) {
            if (bankIcon.code.equals(code)) return bankIcon;
        }
        throw new NotificationException("Código para o ícone selecionado não suportada :: " + code);
    }

    public static BankIcon toName(String name) {
        for (BankIcon bankIcon : BankIcon.values()) {
            if (bankIcon.name().equals(name)) return bankIcon;
        }
        throw new NotificationException("Código para o ícone selecionado não suportada :: " + name);
    }
}
