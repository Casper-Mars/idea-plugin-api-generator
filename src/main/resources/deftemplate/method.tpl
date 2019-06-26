
    @RequestMapping(value = "${URL}", method = RequestMethod.${METHODTYPE})
    @ApiOperation(value = "${DESCRIPTION}", httpMethod = "${METHODTYPE}", response = ${RESPONSE_NAME}.class, consumes = "${CONTENT_TYPE}")
    @ApiImplicitParams({
        ${PARAM_LIST}
    })
    public String ${NAME}(${REQUEST_BODY}) {
        return null;
    }
    