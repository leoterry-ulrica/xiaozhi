<?xml version="1.0"?>
<?mso-application progid="Excel.Sheet"?>
<Workbook xmlns="urn:schemas-microsoft-com:office:spreadsheet"
	xmlns:o="urn:schemas-microsoft-com:office:office" xmlns:x="urn:schemas-microsoft-com:office:excel"
	xmlns:ss="urn:schemas-microsoft-com:office:spreadsheet" xmlns:html="http://www.w3.org/TR/REC-html40">
	<DocumentProperties xmlns="urn:schemas-microsoft-com:office:office">
		<Created>2006-09-16T00:00:00Z</Created>
		<LastSaved>2017-03-22T07:19:29Z</LastSaved>
		<Version>15.00</Version>
	</DocumentProperties>
	<OfficeDocumentSettings xmlns="urn:schemas-microsoft-com:office:office">
		<AllowPNG />
		<RemovePersonalInformation />
	</OfficeDocumentSettings>
	<ExcelWorkbook xmlns="urn:schemas-microsoft-com:office:excel">
		<WindowHeight>8010</WindowHeight>
		<WindowWidth>14805</WindowWidth>
		<WindowTopX>240</WindowTopX>
		<WindowTopY>105</WindowTopY>
		<ProtectStructure>False</ProtectStructure>
		<ProtectWindows>False</ProtectWindows>
	</ExcelWorkbook>
	<Styles>
		<Style ss:ID="Default" ss:Name="Normal">
			<Alignment ss:Vertical="Bottom" />
			<Borders />
			<Font ss:FontName="宋体" x:CharSet="134" ss:Size="11" ss:Color="#000000" />
			<Interior />
			<NumberFormat />
			<Protection />
		</Style>
		<Style ss:ID="s16">
			<Font ss:FontName="微软雅黑" x:CharSet="134" x:Family="Swiss"
				ss:Size="11" ss:Color="#000000" ss:Bold="1" />
			<Interior ss:Color="#C4D79B" ss:Pattern="Solid" />
		</Style>
	</Styles>
	<Worksheet ss:Name="Sheet1">
		<Table ss:ExpandedColumnCount="26" ss:ExpandedRowCount="${taskList?size+2}"
			x:FullColumns="1" x:FullRows="1" ss:DefaultColumnWidth="54"
			ss:DefaultRowHeight="13.5">
			<Column ss:Index="2" ss:AutoFitWidth="0" ss:Width="69" />
			<Column ss:Index="5" ss:AutoFitWidth="0" ss:Width="73.5" />
			<Column ss:Index="16" ss:AutoFitWidth="0" ss:Width="80.25" />
			<Row ss:Height="15">
				<Cell ss:StyleID="s16">
					<Data ss:Type="String">序号</Data>
				</Cell>
				<Cell ss:StyleID="s16">
					<Data ss:Type="String">省</Data>
				</Cell>
				<Cell ss:StyleID="s16">
					<Data ss:Type="String">市</Data>
				</Cell>
				<Cell ss:StyleID="s16">
					<Data ss:Type="String">县</Data>
				</Cell>
				<Cell ss:StyleID="s16">
					<Data ss:Type="String">客户名称</Data>
				</Cell>
				<Cell ss:StyleID="s16">
					<Data ss:Type="String">项目名称</Data>
				</Cell>
				<Cell ss:StyleID="s16">
					<Data ss:Type="String">业务类型</Data>
				</Cell>
				<Cell ss:StyleID="s16">
					<Data ss:Type="String">相关部门</Data>
				</Cell>
				<Cell ss:StyleID="s16">
					<Data ss:Type="String">负责人</Data>
				</Cell>
				<Cell ss:StyleID="s16">
					<Data ss:Type="String">助理</Data>
				</Cell>
				<Cell ss:StyleID="s16">
					<Data ss:Type="String">任务目标</Data>
				</Cell>
				<Cell ss:StyleID="s16">
					<Data ss:Type="String">客户关注点</Data>
				</Cell>
				<Cell ss:StyleID="s16">
					<Data ss:Type="String">任务类型</Data>
				</Cell>
				<Cell ss:StyleID="s16">
					<Data ss:Type="String">受众</Data>
				</Cell>
				<Cell ss:StyleID="s16">
					<Data ss:Type="String">具体说明</Data>
				</Cell>
				<Cell ss:StyleID="s16">
					<Data ss:Type="String">现场</Data>
				</Cell>
				<Cell ss:StyleID="s16">
					<Data ss:Type="String">模板</Data>
				</Cell>
				<Cell ss:StyleID="s16">
					<Data ss:Type="String">硬件</Data>
				</Cell>
				<Cell ss:StyleID="s16">
					<Data ss:Type="String">时间节点</Data>
				</Cell>
				<Cell ss:StyleID="s16">
					<Data ss:Type="String">发布时间</Data>
				</Cell>
				<Cell ss:StyleID="s16">
					<Data ss:Type="String">预期人员</Data>
				</Cell>
				<Cell ss:StyleID="s16">
					<Data ss:Type="String">负责人反馈</Data>
				</Cell>
				<Cell ss:StyleID="s16">
					<Data ss:Type="String">客户反馈</Data>
				</Cell>
				<Cell ss:StyleID="s16">
					<Data ss:Type="String">响应时间</Data>
				</Cell>
				<Cell ss:StyleID="s16">
					<Data ss:Type="String">任务状态</Data>
				</Cell>
				<Cell ss:StyleID="s16">
					<Data ss:Type="String">执行人</Data>
				</Cell>
			</Row>
			<#list taskList as task>
			<Row>
				<Cell>
					<Data ss:Type="Number">${task.index}</Data>
				</Cell>
				<Cell>
					<Data ss:Type="String">${task.province}</Data>
				</Cell>
				<Cell>
					<Data ss:Type="String">${task.city}</Data>
				</Cell>
				<Cell>
					<Data ss:Type="String">${task.county}</Data>
				</Cell>
				<Cell>
					<Data ss:Type="String">${task.delegateUnit}</Data>
				</Cell>
				<Cell>
					<Data ss:Type="String">${task.projectName}</Data>
				</Cell>
				<Cell>
					<Data ss:Type="String">${task.businessType}</Data>
				</Cell>
				<Cell>
					<Data ss:Type="String">${task.orgMaster + "," + task.orgCoo + "," + task.orgOtherCoo}</Data>
				</Cell>
				<Cell>
					<Data ss:Type="String">${task.projectManager}</Data>
				</Cell>
				<Cell>
					<Data ss:Type="String">${task.projectAssistant}</Data>
				</Cell>
				<Cell>
					<Data ss:Type="String">${task.target}</Data>
				</Cell>
				<Cell>
					<Data ss:Type="String">${task.focus}</Data>
				</Cell>
				<Cell>
					<Data ss:Type="String">${task.type}</Data>
				</Cell>
				<Cell>
					<Data ss:Type="String">${task.audience}</Data>
				</Cell>
				<Cell>
					<Data ss:Type="String">${task.content}</Data>
				</Cell>
				<Cell>
					<Data ss:Type="String">${task.localSupport}</Data>
				</Cell>
				<Cell>
					<Data ss:Type="String">${task.solutionTemp}</Data>
				</Cell>
				<Cell>
					<Data ss:Type="String">${task.hardwareReq}</Data>
				</Cell>
				<Cell>
					<Data ss:Type="String">${(task.beginTime?string("yyyy-MM-dd"))!'无数据' + "-" + (task.endTime?string("yyyy-MM-dd"))!'无数据'}</Data>
				</Cell>
				<Cell>
					<Data ss:Type="String">${(task.createTime?string("yyyy-MM-dd"))!'无数据'}</Data>
				</Cell>
				<Cell>
					<Data ss:Type="String">${task.expectPeople}</Data>
				</Cell>
				<Cell>
					<Data ss:Type="String">${task.headFeedback}</Data>
				</Cell>
				<Cell>
					<Data ss:Type="String">${task.customerFeedback}</Data>
				</Cell>
				<Cell>
					<Data ss:Type="String">${(task.responseTime?string("yyyy-MM-dd"))!'无数据'}</Data>
				</Cell>
				<Cell>
					<Data ss:Type="String">${task.status}</Data>
				</Cell>
				<Cell>
					<Data ss:Type="String">${task.expectPeople}</Data>
				</Cell>
			</Row>
			</#list>
		</Table>
		<WorksheetOptions xmlns="urn:schemas-microsoft-com:office:excel">
			<PageSetup>
				<Header x:Margin="0.3" />
				<Footer x:Margin="0.3" />
				<PageMargins x:Bottom="0.75" x:Left="0.7" x:Right="0.7"
					x:Top="0.75" />
			</PageSetup>
			<Print>
				<ValidPrinterInfo />
				<PaperSizeIndex>9</PaperSizeIndex>
				<HorizontalResolution>600</HorizontalResolution>
				<VerticalResolution>600</VerticalResolution>
			</Print>
			<Selected />
			<LeftColumnVisible>5</LeftColumnVisible>
			<Panes>
				<Pane>
					<Number>3</Number>
					<ActiveRow>2</ActiveRow>
					<ActiveCol>25</ActiveCol>
				</Pane>
			</Panes>
			<ProtectObjects>False</ProtectObjects>
			<ProtectScenarios>False</ProtectScenarios>
		</WorksheetOptions>
	</Worksheet>
</Workbook>
