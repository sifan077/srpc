package com.sifan.srpc.register;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

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
    private String weight;
    private String loadBalance;
    private List<MethodItem> methodList;
}

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
class MethodItem {
    private String methodName;
    private String returnType;
    private List<String> parameterTypes;
}
