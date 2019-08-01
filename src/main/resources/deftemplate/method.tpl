
    @RequestMapping(value = "${URL}", method = RequestMethod.${METHODTYPE})
    @ApiOperation(value = "${DESCRIPTION}", httpMethod = "${METHODTYPE}", consumes = "${CONTENT_TYPE}")
    @ApiImplicitParams({
        ${PARAM_LIST}
    })
    public ${RESPONSE} ${NAME}(${REQUEST_BODY}) {
        return null;
    }
    