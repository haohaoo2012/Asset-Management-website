package com.nashtech.rootkies.generator;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import org.hibernate.HibernateException;
import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.id.IdentifierGenerator;
import lombok.Getter;
import lombok.Setter;

import com.nashtech.rootkies.model.Asset;

@Getter
@Setter
public class AssetCodeGenerator implements IdentifierGenerator {

    private Short maxId = 0;

    @Override
    public Serializable generate(SharedSessionContractImplementor arg0, Object arg1) throws HibernateException {
        Connection connection = arg0.connection();
        Asset asset = (Asset) arg1;
        try {
            PreparedStatement ps = connection.prepareStatement("select * from assets a where a.assetcode like '"
                    + asset.getCategory().getCategoryCode() + "%' order by a.assetcode desc ");
            ResultSet rs = ps.executeQuery();

            int flag = 0;

            while (rs.next()) {
                String numberPart = rs.getString("assetcode").substring(asset.getCategory().getCategoryCode().length());
                try {
                    Short numPart = Short.parseShort(numberPart);
                    this.setMaxId(numPart);
                    flag += 1;
                    break;
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            if (flag == 0) {
                this.setMaxId((short) 1);
            } else {
                this.setMaxId((short) (this.getMaxId() + 1));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return asset.getCategory().getCategoryCode() + String.format("%06d", this.getMaxId());
    }

}
