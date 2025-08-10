package com.stjude.directory.model;

import java.util.List;
import lombok.Data;

@Data
public class CommitteeCard {
    private String title;
    private List<CommitteePosition> positions;
}
