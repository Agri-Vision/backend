package com.research.agrivision.api.adapter.integration.integrate.tool.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Patch {
    public Ndvi ndvi;
    public Rendvi rendvi;
    public Cire cire;
    public Pri pri;
    public int row;
    public int column;
}
