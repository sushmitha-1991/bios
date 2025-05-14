package com.dsp.bios.dto.newbillentry;

import com.dsp.bios.dto.newbillentry.contants.Contants;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.time.Year;
import java.util.List;

public class ConditionalFieldsValidator implements ConstraintValidator<ValidConditionalFields, EntryDataDTO> {

    private final List<String> introducedBy = List.of(Contants.GOVERNMENT,Contants.PRIVATE_MEMBER);
    private final List<String> actValidation=List.of(
            Contants.ORDINARY_BILL_ABBR,
            Contants.FINANCIAL_BILL_ABBR,
            Contants.MONEY_BILL_ABBR,
            Contants.ORDINANCE_REPLACING_BILL_ABBR);
    private final List<String> caValidation=List.of(
            Contants.CONSTITUTIONAL_AMENDMENT_BILL_ABBR);

    private final List<String> ABRadio = List.of(Contants.FINANCIAL_BILL_ABBR);
    @Override
    public void initialize(ValidConditionalFields constraintAnnotation) {
    }

    @Override
    public boolean isValid(EntryDataDTO dto, ConstraintValidatorContext context) {
        boolean valid=true;

        valid = validateHouseData(dto, context, valid);
        valid = validateMinistryInCharge(dto, context, valid);
        valid = validateIntrodudedBy(dto, context, valid);
        valid = checkMainField(dto, context, valid);
        if(!valid) return valid;
        if (dto.getBillYear() != null) {
            valid = isYearBetween1947toCurrent(dto.getBillYear(), context, "billYear",valid);
        }
        if(actValidation.contains(dto.getBillStatusAbbr())){
            valid=doActValidation(dto,context,valid);
        }
        if(caValidation.contains(dto.getBillStatusAbbr())){
            valid=doCAValidation(dto,context,valid);
        }
        valid = validateRadioABForFinanceBill(dto, context, valid);
        valid = validateBillStatus(dto, context, valid);
        return valid;
    }

    private boolean checkMainField(EntryDataDTO dto, ConstraintValidatorContext context, boolean valid) {
        if (  dto.getBillCategory() == null ) {
            context.buildConstraintViolationWithTemplate("billCategory should not be null")
                    .addPropertyNode("billCategory")
                    .addConstraintViolation();
            valid = false;
        }
        if (  dto.getBillCategoryAbbr() == null ) {
            context.buildConstraintViolationWithTemplate("billCategoryAbbr should not be null")
                    .addPropertyNode("billCategoryAbbr")
                    .addConstraintViolation();
            valid = false;
        }
        if (  dto.getBillNumber() == null ) {
            context.buildConstraintViolationWithTemplate("billNumber should not be null")
                    .addPropertyNode("billNumber")
                    .addConstraintViolation();
            valid = false;
        }
        if (  dto.getBillYear() == null ) {
            context.buildConstraintViolationWithTemplate("billYear should not be null")
                    .addPropertyNode("billYear")
                    .addConstraintViolation();
            valid = false;
        }
        if (  dto.getShortTitle() == null ) {
            context.buildConstraintViolationWithTemplate("shortTitle should not be null")
                    .addPropertyNode("shortTitle")
                    .addConstraintViolation();
            valid = false;
        }
        if (  dto.getBillStatusAbbr() == null ) {
            context.buildConstraintViolationWithTemplate("billStatusAbbr should not be null")
                    .addPropertyNode("billStatusAbbr")
                    .addConstraintViolation();
            valid = false;
        }
        if (  dto.getDateOfIntroduction() == null ) {
            context.buildConstraintViolationWithTemplate("dateOfIntroduction should not be null")
                    .addPropertyNode("dateOfIntroduction")
                    .addConstraintViolation();
            valid = false;
        }
        return valid;
    }

    private boolean validateMinistryInCharge(EntryDataDTO dto, ConstraintValidatorContext context, boolean valid) {
        if (  dto.getMinistryInChargeId() == null ) {
            context.buildConstraintViolationWithTemplate("ministryInChargeId should not be null")
                    .addPropertyNode("ministryInChargeId")
                    .addConstraintViolation();
            valid = false;
        }
        return valid;
    }

    private boolean validateBillStatus(EntryDataDTO dto, ConstraintValidatorContext context, boolean valid) {
        if(dto.getPendingStatusAbbr()!=null){
            if( Contants.PENDING_BILL_STATUS_ABBR.equalsIgnoreCase(dto.getBillStatusAbbr())){
                valid = validatePendingBillStatus(dto, context, valid);
            }
            if(Contants.WITHDRAW_STATUS_ABBR.equalsIgnoreCase(dto.getBillStatusAbbr())){
                valid = validateWithdrawBillStatus(dto, context, valid);
            }
            if(Contants.NEGATIVED_STATUS_ABBR.equalsIgnoreCase(dto.getBillStatusAbbr())){
                valid = validateNegativedBillStatus(dto, context, valid);
            }
            if(Contants.ASSENTED_STATUS_ABBR.equalsIgnoreCase(dto.getBillStatusAbbr())){
                valid = validateAssentedBillStatus(dto, context, valid);
            }
            if(Contants.PASSED_STATUS_ABBR.equalsIgnoreCase(dto.getBillStatusAbbr())){
                valid = validatePassedBillStatus(dto, context, valid);
            }
        }
        return valid;
    }

    private boolean validateRadioABForFinanceBill(EntryDataDTO dto, ConstraintValidatorContext context, boolean valid) {
        if(ABRadio.contains(dto.getBillCategoryAbbr())){
                if ( dto.getRadioA() == null ) {
                    context.buildConstraintViolationWithTemplate("Radio A  must not be for for this bill Type")
                            .addPropertyNode("radioA")
                            .addConstraintViolation();
                    valid =false;
            }
                if ( dto.getRadioB() == null ) {
                context.buildConstraintViolationWithTemplate("Radio B and must not be for for this bill Type")
                        .addPropertyNode("radioB")
                        .addConstraintViolation();
                valid =false;
            }
        }
        return valid;
    }

    private boolean validateIntrodudedBy(EntryDataDTO dto, ConstraintValidatorContext context, boolean valid) {

        if (dto.getIntroducedBy()==null ) {
            context.buildConstraintViolationWithTemplate("introducedBy should not be null")
                    .addPropertyNode("introducedBy")
                    .addConstraintViolation();
            return false;
        }
        if (!introducedBy.contains(dto.getIntroducedBy()) ) {
            context.buildConstraintViolationWithTemplate("introducedBy should be private or government")
                    .addPropertyNode("introducedBy")
                    .addConstraintViolation();
            valid = false;
        }

        if (Contants.PRIVATE_MEMBER.equals(dto.getIntroducedBy()) && dto.getMemberId()==null  )  {
            context.buildConstraintViolationWithTemplate("memberId not be null for private Member")
                    .addPropertyNode("memberId")
                    .addConstraintViolation();
            valid = false;
        }
        return valid;
    }

    private  boolean validateHouseData(EntryDataDTO dto, ConstraintValidatorContext context, boolean valid) {



        String houseId= dto.getBillIntroducedInHouseAbbr();
        if(houseId==null || dto.getBillIntroducedInHouse()==null ) {
            context.buildConstraintViolationWithTemplate("Bill introduced should not be null")
                    .addPropertyNode("billIntroducedInHouseAbbr")
                    .addConstraintViolation();
           return false;
        }


        if (!(houseId.equals( Contants.RAJ_SABHA_HOUSE_ABBR)||houseId.equals(Contants.LOK_SABHA_HOUSE_ABBR))) {
            context.buildConstraintViolationWithTemplate("Bill introducted should be lok sabha or rajya sabha")
                    .addPropertyNode("billIntroducedInHouseId")
                    .addConstraintViolation();
            valid =false;
        }
        else {

            if (Contants.LOK_SABHA_HOUSE_ABBR.equalsIgnoreCase(dto.getBillIntroducedInHouseAbbr())) {
                valid = validateLokSabhaHouseData(dto, context, valid);
            }

            if (Contants.RAJ_SABHA_HOUSE_ABBR.equalsIgnoreCase(dto.getBillIntroducedInHouseAbbr()) ) {
                valid = validateRajyaSabhaHouseData(dto, context, valid);
            }
        }
        return valid;
    }

    private static boolean validateRajyaSabhaHouseData(EntryDataDTO dto, ConstraintValidatorContext context, boolean valid) {
        if (dto.getRajSabhaSessionId() == null) {
            context.buildConstraintViolationWithTemplate("RajSabhaSessionId should not be null  rajya sabha")
                    .addPropertyNode("rajSabhaSessionId")
                    .addConstraintViolation();
            valid = false;
        }
        return valid;
    }

    private static boolean validateLokSabhaHouseData(EntryDataDTO dto, ConstraintValidatorContext context, boolean valid) {
        if(  dto.getLokSabhaNumberId() == null ) {
            context.buildConstraintViolationWithTemplate("LokSabhaNumberId must not be  null for lok sabha session")
                    .addPropertyNode("lokSabhaNumberId")
                    .addConstraintViolation();
            valid = false;
        }
        if(  dto.getLokSabhaSessionNumberId() == null ) {
            context.buildConstraintViolationWithTemplate("lkSabhaSessionNumberId is null of lok sabha session")
                    .addPropertyNode("lokSabhaSessionNumberId")
                    .addConstraintViolation();
            valid = false;
        }
        return valid;
    }

    private boolean validatePassedBillStatus(EntryDataDTO dto, ConstraintValidatorContext context, boolean valid) {
        if(dto.getPassedIn()==null) {

            context.buildConstraintViolationWithTemplate("passedIn must not be for for this Pending Status Type")
                    .addPropertyNode("passedIn")
                    .addConstraintViolation();
            valid = false;
        }

        if(dto.getPassedDate()==null) {


            context.buildConstraintViolationWithTemplate("passedDate must not be for for this Pending Status Type")
                    .addPropertyNode("passedDate")
                    .addConstraintViolation();
            valid = false;
        }
        return valid;
    }

    private boolean validateAssentedBillStatus(EntryDataDTO dto, ConstraintValidatorContext context, boolean valid) {
        if(dto.getDateOfAssented()==null) {


            context.buildConstraintViolationWithTemplate("dateOfAssented must not be for for this Pending Status Type")
                    .addPropertyNode("dateOfAssented")
                    .addConstraintViolation();
            valid = false;
        }


        return valid;
    }

    private boolean validateNegativedBillStatus(EntryDataDTO dto, ConstraintValidatorContext context, boolean valid) {
        if(dto.getDateOfNegatived()==null) {


            context.buildConstraintViolationWithTemplate("dateOfNegatived  must not be for for this Pending Status Type")
                    .addPropertyNode("dateOfNegatived")
                    .addConstraintViolation();
            valid = false;
        }


        return valid;
    }

    private boolean validateWithdrawBillStatus(EntryDataDTO dto, ConstraintValidatorContext context, boolean valid) {
        if(dto.getDateOfWithdrawn()==null) {


            context.buildConstraintViolationWithTemplate("date of withdrawn  must not be for for this Pending Status Type")
                    .addPropertyNode("dateOfWithdrawn")
                    .addConstraintViolation();
            valid = false;
        }
        return valid;
    }

    private static boolean validatePendingBillStatus(EntryDataDTO dto, ConstraintValidatorContext context, boolean valid) {
        if(dto.getPendingStatus()==null) {


            context.buildConstraintViolationWithTemplate("Pending Status  must not be for for this Pending Status Type")
                    .addPropertyNode("pendingStatus")
                    .addConstraintViolation();
            valid = false;
        }

        if(dto.getPendingStatusDate()==null) {


            context.buildConstraintViolationWithTemplate("Pending Status Date must not be for for this Pending Status Type")
                    .addPropertyNode("pendingStatusDate")
                    .addConstraintViolation();
            valid = false;
        }
        return valid;
    }

    private boolean doCAValidation(EntryDataDTO dto, ConstraintValidatorContext context,Boolean valid) {

        if(dto.getIsCANo() ==null ){
            context.buildConstraintViolationWithTemplate("isCANo must Not be null")
                    .addPropertyNode("isCANo")
                    .addConstraintViolation();
            return false;
        }

        if(dto.getIsCANo()  && dto.getCaNo()==null){
            context.buildConstraintViolationWithTemplate("CA Number must Not be for for this bill Type")
                    .addPropertyNode("caNo")
                    .addConstraintViolation();
            valid= false;
        }


        if(dto.getIsCANo() && dto.getCaYear()==null ) {
            context.buildConstraintViolationWithTemplate("CA year must Not be null")
                    .addPropertyNode("caYear")
                    .addConstraintViolation();
            valid= false;
        }
        if(dto.getIsActNo() && dto.getCaYear()!=null  ) {
            Boolean yearValid = isYearBetween1947toCurrent(dto.getActYear(),context,"caYear",valid);
            if(!yearValid){
                valid=yearValid;
            }
        }
        return  valid;
    }
    private boolean doActValidation(EntryDataDTO dto, ConstraintValidatorContext context,Boolean valid) {

        if( dto.getIsActNo() ==null ){
            context.buildConstraintViolationWithTemplate("isActNo must Not be null")
                    .addPropertyNode("isActNo")
                    .addConstraintViolation();
            return false;
        }
        if(dto.getIsActNo() && dto.getActNo()==null){
            context.buildConstraintViolationWithTemplate("Act Number must Not be for for this bill Type")
                    .addPropertyNode("actNo")
                    .addConstraintViolation();
            valid= false;
        }


        if(dto.getIsActNo() && dto.getActYear()==null ) {
            context.buildConstraintViolationWithTemplate("Act year must Not be null")
                    .addPropertyNode("isActNo")
                    .addConstraintViolation();
            valid= false;
        }
        if(dto.getIsActNo() && dto.getActYear()!=null  ) {
            Boolean yearValid = isYearBetween1947toCurrent(dto.getActYear(),context,"actYear",valid);
            if(!yearValid){
                valid=yearValid;
            }
        }
        return  valid;
    }

    private boolean isYearBetween1947toCurrent(Long year, ConstraintValidatorContext context,String fieldName,Boolean valid) {
        int currentYear = Year.now().getValue();
        if (year < 1947 || year > currentYear) {
            context.buildConstraintViolationWithTemplate("The year must be between 1947 and the current year")
                    .addPropertyNode(fieldName)
                    .addConstraintViolation();
            valid= false;
        }
        return valid;
    }
}