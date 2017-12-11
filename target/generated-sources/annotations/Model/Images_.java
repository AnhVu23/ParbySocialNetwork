package Model;

import java.util.Date;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2017-12-11T20:13:22")
@StaticMetamodel(Images.class)
public class Images_ { 

    public static volatile SingularAttribute<Images, Integer> imageId;
    public static volatile SingularAttribute<Images, Integer> uploader;
    public static volatile SingularAttribute<Images, String> imgData;
    public static volatile SingularAttribute<Images, String> caption;
    public static volatile SingularAttribute<Images, Date> time;

}