package com.sifan.srpc.register;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class InterfaceItem {
    private String interfaceName;
    private String version;
    private List<String> group;
    private String host;
    private Integer port;
    private Double weight;
    private Map<String, Object> meta;
}