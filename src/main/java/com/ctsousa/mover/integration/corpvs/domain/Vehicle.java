package com.ctsousa.mover.integration.corpvs.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class Vehicle {
    @JsonProperty("vei_id")
    private Long veiId;

    @JsonProperty("eqp_srn")
    private Long eqpSrn;

    @JsonProperty("tpv_id")
    private String tpvId;

    @JsonProperty("cli_id")
    private Long cliId;

    @JsonProperty("cli_id1")
    private Long cliId1;

    @JsonProperty("vei_cox")
    private Integer veiCox;

    @JsonProperty("vei_dsc")
    private String veiDsc;

    @JsonProperty("vei_plc")
    private String veiPlc;

    @JsonProperty("vei_mrc")
    private String veiMrc;

    @JsonProperty("vei_mdl")
    private String veiMdl;

    @JsonProperty("vei_cor")
    private String veiCor;

    @JsonProperty("vei_chs")
    private String veiChs;

    @JsonProperty("vei_ano")
    private String veiAno;

    @JsonProperty("ico_id")
    private String icoId;

    @JsonProperty("vei_fld")
    private Integer veiFld;

    @JsonProperty("vei_trabalho")
    private Integer veiTrabalho;

    @JsonProperty("obs_vei")
    private String obsVei;

    @JsonProperty("teste")
    private String teste;

    @JsonProperty("vei_ano_fab")
    private String veiAnoFab;

    @JsonProperty("vei_ren")
    private String veiRen;

    @JsonProperty("ign_off")
    private LocalDateTime ignOff;

    @JsonProperty("cor_icone")
    private String corIcone;

    @JsonProperty("tipo_icone")
    private String tipoIcone;

    @JsonProperty("sensor_odometro")
    private String sensorOdometro;

    @JsonProperty("sensor_temp")
    private String sensorTemp;

    @JsonProperty("horas_uso_dia")
    private String horasUsoDia;

    @JsonProperty("id_tipo_combustivel")
    private Integer idTipoCombustivel;

    @JsonProperty("tipo_consumo")
    private String tipoConsumo;

    @JsonProperty("consumo")
    private Double consumo;

    @JsonProperty("identificador_motorista")
    private String identificadorMotorista;

    @JsonProperty("checa_cerca")
    private String checaCerca;

    @JsonProperty("restrito")
    private String restrito;

    @JsonProperty("iter_id")
    private Integer iterId;

    @JsonProperty("deleted_at")
    private LocalDateTime deletedAt;

    @JsonProperty("uf")
    private String uf;

    @JsonProperty("cidade")
    private String cidade;

    @JsonProperty("meta_fuel")
    private Double metaFuel;

    @JsonProperty("tank_capacity")
    private Double tankCapacity;

    @JsonProperty("line_id")
    private Integer lineId;

    @JsonProperty("controller_api")
    private Integer controllerApi;

    @JsonProperty("log_api")
    private Integer logApi;

    @JsonProperty("price_fuel")
    private Double priceFuel;

    @JsonProperty("code_integration")
    private String codeIntegration;

    @JsonProperty("vei_bus")
    private Integer veiBus;

    @JsonProperty("gta_multa")
    private Integer gtaMulta;
}
