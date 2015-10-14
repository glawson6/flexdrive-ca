package com.ca.flex;

import au.com.bytecode.opencsv.CSVWriter;
import org.mule.api.MuleMessage;
import org.mule.api.transformer.TransformerException;
import org.mule.transformer.AbstractMessageTransformer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.StringWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by tap on 7/28/15.
 */
public class UnitInfoToWriterTransformer extends AbstractMessageTransformer {
    private final Logger logger = LoggerFactory.getLogger(UnitInfoToWriterTransformer.class);

    String[] headers = {"VehicleID", "Year", "Make", "Model", "VehicleType",
            "VINNumber", "Status", "Odometer", "OriginalOdometer",
            "Colour", "Price", "LicenseNumber", "LicenseExpiry", "LicenseState",
            "TankSize", "Memo", "Doors", "FuelLevel",
            "FuelType", "Transmission", "Body", "ServiceInterval", "Active"};

    @Override
    public Object transformMessage(MuleMessage message, String outputEncoding) throws TransformerException {
        logger.info("######### Entering UnitInfoToWriterTransformer ########");
        List<HashMap> unitInfoList = (List<HashMap>) message.getPayload();
        logger.info("unitInfoList => {}",unitInfoList.toString());
        StringWriter stringWriter = new StringWriter();
        CSVWriter writer = new CSVWriter(stringWriter);
        List<String[]> buyerStrings = convertUnitInfo(unitInfoList);
        writer.writeAll(buyerStrings);
        return stringWriter;
    }

    private List<String[]> convertUnitInfo(List<HashMap> unitInfoList) {
        List<String[]> converted = new ArrayList<>();
        converted.add(headers);
        for (HashMap unitInfo : unitInfoList) {
            converted.add(convertMapToCSVLine(unitInfo));
        }
        return converted;
    }

    private String[] convertMapToCSVLine(HashMap unitInfo){
        HashMap description = (HashMap)unitInfo.get("description");
        HashMap engine = (HashMap)description.get("engine");
        HashMap odometer = (HashMap)unitInfo.get("odometer");
        HashMap exteriorColor = (HashMap)description.get("exteriorColor");

        logger.info("description {}",description.toString());
        logger.info("engine {}",engine.toString());
        logger.info("odometer {}",odometer.toString());
        logger.info("exteriorColor {}",exteriorColor.toString());

        String [] line = {unitInfo.get("customerReferenceId").toString(), description.get("modelYear").toString(),
                description.get("make").toString(), description.get("model").toString(), engine.get("type").toString(),
                unitInfo.get("vin").toString(), "STAUS", odometer.get("reading").toString(), "ORIGINAL ODOMETER",
                exteriorColor.get("code").toString(),"PRICE", "LICENSENUMBER", "LICENSEEXPIRIY", "LICENSESTATE",
                "TANKSIZE", "MEMO", "DOORS", "FUELLEVEL",
                engine.get("fuel").toString(),"TRANSMISSION", "BODY", "SEVICEINTERVAL", "ACTIVE"};

        return line;
    }
}
