package fi.nls.oskari.control.statistics.plugins.db;

import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.ResultType;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * MyBatis Mapper for the SQL table oskari_statistical_plugin_layers
 */
public interface DatasourceLayerMapper {

    @Select("SELECT s.datasource_id, s.layer_id, s.config, l.locale, gl.order_number FROM oskari_statistical_datasource_regionsets s " +
            " JOIN oskari_maplayer l ON l.id = s.layer_id " +
            " JOIN oskari_maplayer_group_link gl ON l.id = gl.maplayerid " +
            "  WHERE " +
            " s.datasource_id = #{datasourceId}")
    @Results({
            @Result(property = "datasourceId", column = "datasource_id"),
            @Result(property = "maplayerId", column = "layer_id"),
            @Result(property = "orderNumber", column = "order_number")
    })
    @ResultType(DatasourceLayer.class)
    List<DatasourceLayer> getLayersForDatasource(long datasourceId);
    /*
    {"statistics":{"featuresUrl":"http://dev.paikkatietoikkuna.fi/geoserver/oskari/wfs","regionIdTag":"kuntakoodi","nameIdTag":"kuntanimi"}}
     */
}
