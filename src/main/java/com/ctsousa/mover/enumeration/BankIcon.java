package com.ctsousa.mover.enumeration;

import com.ctsousa.mover.core.exception.notification.NotificationException;
import lombok.Getter;

@Getter
public enum BankIcon {
    BANK_BB(1,"Banco do brasil", "https://img.logo.dev/bb.com.br?token=pk_Ld5EdtnrTYGgDMZ4-w9MGg&size=128&format=png"),
    BANK_CAIXA(2,"Caixa econômica", "https://img.logo.dev/caixa.com.br?token=pk_Ld5EdtnrTYGgDMZ4-w9MGg&size=128&format=png"),
    BANK_BRADESCO(3,"Bradesco", "https://img.logo.dev/bradesco.com.br?token=pk_Ld5EdtnrTYGgDMZ4-w9MGg&size=128&format=png"),
    BANK_ITAU(4,"Itaú", "https://img.logo.dev/itau.com.br?token=pk_Ld5EdtnrTYGgDMZ4-w9MGg&size=128&format=png"),
    BANK_SANTANDER(5,"Santander", "https://img.logo.dev/santander.com.br?token=pk_Ld5EdtnrTYGgDMZ4-w9MGg&size=128&format=png"),
    BANK_SAFRA(6,"Banco safra","https://img.logo.dev/bancosafra.com.br?token=pk_Ld5EdtnrTYGgDMZ4-w9MGg&size=128&format=png"),
    BANK_BANRISUL(7,"Banrisul","https://img.logo.dev/banrisul.com.br?token=pk_Ld5EdtnrTYGgDMZ4-w9MGg&size=128&format=png"),
    BANK_BNB(8,"Banco do nordeste", "https://img.logo.dev/bnb.com.br?token=pk_Ld5EdtnrTYGgDMZ4-w9MGg&size=128&format=png"),
    BANK_BASA(9,"Banco basa", "https://img.logo.dev/basa.com.br?token=pk_Ld5EdtnrTYGgDMZ4-w9MGg&size=128&format=png"),
    BANK_VOTORANTIM(10,"Banco Votorantim", "https://img.logo.dev/bv.com.br?token=pk_Ld5EdtnrTYGgDMZ4-w9MGg&size=128&format=png"),
    BANK_INTER(11,"Banco inter", "https://img.logo.dev/bancointer.com.br?token=pk_Ld5EdtnrTYGgDMZ4-w9MGg&size=128&format=png"),
    BANK_ORIGINAL(12,"Banco original","https://img.logo.dev/original.com.br?token=pk_Ld5EdtnrTYGgDMZ4-w9MGg&size=128&format=png"),
    BANK_BTG_PACTUAL(13,"Banco BTG Pactual", "https://img.logo.dev/btgpactual.com.br?token=pk_Ld5EdtnrTYGgDMZ4-w9MGg&size=128&format=png"),
    BANK_DAYCOVAL(14,"Banco daycoval", "https://img.logo.dev/daycoval.com.br?token=pk_Ld5EdtnrTYGgDMZ4-w9MGg&size=128&format=png"),
    BANK_PAN(15,"Banco pan", "https://img.logo.dev/bancopan.com.br?token=pk_Ld5EdtnrTYGgDMZ4-w9MGg&size=128&format=png"),
    BANK_SICREDI(16,"Banco sicredi", "https://img.logo.dev/sicredi.com.br?token=pk_Ld5EdtnrTYGgDMZ4-w9MGg&size=128&format=png"),
    BANK_SICOOB(17,"Banco Sicoob", "https://img.logo.dev/sicoob.com.br?token=pk_Ld5EdtnrTYGgDMZ4-w9MGg&size=128&format=png"),
    BANK_BMG(18,"Banco BMG", "https://img.logo.dev/bancobmg.com.br?token=pk_Ld5EdtnrTYGgDMZ4-w9MGg&size=128&format=png"),
    BANK_AGIBANK(19,"AGI Bank", "https://img.logo.dev/agibank.com.br?token=pk_Ld5EdtnrTYGgDMZ4-w9MGg&size=128&format=png"),
    BANK_NUBANK(20,"Nubank", "https://img.logo.dev/nubank.com.br?token=pk_Ld5EdtnrTYGgDMZ4-w9MGg&size=128&format=png"),
    BANK_CORA(21,"Banco cora", "https://img.logo.dev/cora.com.br?token=pk_Ld5EdtnrTYGgDMZ4-w9MGg&size=128&format=png"),
    BANK_C6(22,"C6 Bank", "https://img.logo.dev/c6bank.com.br?token=pk_Ld5EdtnrTYGgDMZ4-w9MGg&size=128&format=png"),
    BANK_PAGBANK(23,"Pagbank", "https://img.logo.dev/pagbank.com.br?token=pk_Ld5EdtnrTYGgDMZ4-w9MGg&size=128&format=png"),
    BANK_NEON(24,"Banco neon", "https://img.logo.dev/neon.com.br?token=pk_Ld5EdtnrTYGgDMZ4-w9MGg&size=128&format=png"),
    BANK_NEXT(25,"Banco next", "https://img.logo.dev/next.com.br?token=pk_Ld5EdtnrTYGgDMZ4-w9MGg&size=128&format=png"),
    BANK_BS2(26,"Banco B2B", "https://img.logo.dev/bs2.com.br?token=pk_Ld5EdtnrTYGgDMZ4-w9MGg&size=128&format=png"),
    BANK_SOFISA(27,"Banco sofisa", "https://img.logo.dev/sofisa.com.br?token=pk_Ld5EdtnrTYGgDMZ4-w9MGg&size=128&format=png"),
    BANK_PICPAY(28,"Picpay", "https://img.logo.dev/picpay.com.br?token=pk_Ld5EdtnrTYGgDMZ4-w9MGg&size=128&format=png"),
    BANK_DIGIO(29,"Digio", "https://img.logo.dev/digio.com.br?token=pk_Ld5EdtnrTYGgDMZ4-w9MGg&size=128&format=png"),
    BANK_WILL(30,"Will bank", "https://img.logo.dev/bancowill.com.br?token=pk_Ld5EdtnrTYGgDMZ4-w9MGg&size=128&format=png"),
    BANK_MERCADO_PAGO(31,"Mercado pago", "https://img.logo.dev/mercadopago.com.br?token=pk_Ld5EdtnrTYGgDMZ4-w9MGg&size=128&format=png"),
    BANK_BANQI(32,"Banco BanQi", "https://img.logo.dev/banqi.com.br?token=pk_Ld5EdtnrTYGgDMZ4-w9MGg&size=128&format=png"),
    BANK_BARI(33,"Banco Bari", "https://img.logo.dev/bari.com.br?token=pk_Ld5EdtnrTYGgDMZ4-w9MGg&size=128&format=png");

    private final Integer code;
    private final String bankName;
    private final String urlImage;

    BankIcon(Integer code, String bankName, String urlImage) {
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
}
