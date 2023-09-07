package com.murat.invoice.generation.service.impl;

import com.murat.invoice.generation.constants.ApplicationConstants;
import com.murat.invoice.generation.exception.ResourceNotFound;
import com.murat.invoice.generation.model.*;
import com.murat.invoice.generation.model.response.InvoiceDetail;
import com.murat.invoice.generation.model.response.InvoiceProductDetail;
import com.murat.invoice.generation.repositories.AdvertiserRepository;
import com.murat.invoice.generation.repositories.InvoiceProductRepository;
import com.murat.invoice.generation.repositories.InvoiceRepository;
import com.murat.invoice.generation.repositories.ProductRepository;
import com.murat.invoice.generation.service.InvoiceService;
import com.murat.invoice.generation.utils.ApplicationUtils;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class InvoiceServiceImpl implements InvoiceService {

    @Value("${invoice.save.local.path}")
    private String localPath;

    @Autowired
    private AdvertiserRepository advertiserRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private InvoiceRepository invoiceRepository;

    @Autowired
    private InvoiceProductRepository invoiceProductRepository;

    @Override
    @Transactional
    public long addInvoice(AddInvoiceRequest invoiceRequest){
        Advertiser advertiser = advertiserRepository.findById(invoiceRequest.getAdvertiserId()).orElseThrow(()->new ResourceNotFound(ApplicationConstants.ADVERTISER_NOT_EXISTS));
        Map<Long,Product> productMap = new HashMap<>();
        invoiceRequest.getProducts().forEach(e->{
            Product product = productRepository.findById(e.getProductId()).orElseThrow(()->new ResourceNotFound(ApplicationConstants.PRODUCT_NOT_EXISTS));
            productMap.put(product.getId(),product);
        });
        Invoice invoice = new Invoice();
        invoice.setAdvertiserId(advertiser.getId());
        invoice.setInvoiceDate(ApplicationUtils.getLocalDate(invoiceRequest.getInvoiceDate()));
        Invoice generatedInvoice = invoiceRepository.save(invoice);
        if(generatedInvoice!=null && generatedInvoice.getId()>0) {
            invoiceRequest.getProducts().forEach(e -> {
                Product product = productMap.get(e.getProductId());
                InvoiceProduct invoiceProduct = new InvoiceProduct();
                invoiceProduct.setInvoiceId(generatedInvoice.getId());
                invoiceProduct.setProductId(product.getId());
                invoiceProduct.setPrice(e.getPrice() > 0 ? e.getPrice() : product.getBasePrice());
                invoiceProduct.setQuantity(e.getQuantity());
                invoiceProductRepository.save(invoiceProduct);
            });
            return generatedInvoice.getId();
        }
        return 0l;
    }

    @Override
    public InvoiceDetail getInvoiceDetail(long invoiceId){
        Invoice invoice = invoiceRepository.findById(invoiceId).orElseThrow(()->new ResourceNotFound(ApplicationConstants.INVOICE_NOT_EXISTS));
        Advertiser advertiser = advertiserRepository.findById(invoice.getAdvertiserId()).orElseThrow(()->new ResourceNotFound(ApplicationConstants.ADVERTISER_NOT_EXISTS));
        List<InvoiceProduct> invoiceProducts = invoiceProductRepository.getInvoiceProductByInvoiceId(invoice.getId());
        InvoiceDetail invoiceDetail = new InvoiceDetail();
        invoiceDetail.setInvoiceId(invoice.getId());
        invoiceDetail.setAdvertiser(advertiser);
        invoiceDetail.setInvoiceDate(ApplicationUtils.getLocalDate(invoice.getInvoiceDate()));
        List<InvoiceProductDetail> invoiceProductDetails = new ArrayList<>();
        double totalPrice=0;
        for(InvoiceProduct e : invoiceProducts){
            Product product = productRepository.findById(e.getProductId()).orElseThrow(()->new ResourceNotFound(ApplicationConstants.PRODUCT_NOT_EXISTS));
            InvoiceProductDetail invoiceProductDetail = new InvoiceProductDetail();
            invoiceProductDetail.setProduct(product);
            invoiceProductDetail.setRate(e.getPrice());
            invoiceProductDetail.setPrice(e.getPrice()*e.getQuantity());
            invoiceProductDetail.setQuantity(e.getQuantity());
            invoiceProductDetails.add(invoiceProductDetail);
            totalPrice=totalPrice+invoiceProductDetail.getPrice();
        }
        invoiceDetail.setInvoiceAmount(totalPrice);
        invoiceDetail.setProductDetails(invoiceProductDetails);
        return invoiceDetail;
    }

    @Override
    public void generateInvoicePdf(long invoiceId){
        InvoiceDetail invoiceDetail = getInvoiceDetail(invoiceId);
        PDDocument invoicePdf = new PDDocument();
        PDPage page = new PDPage();
        invoicePdf.addPage(page);
        try {
            PDPageContentStream cs = new PDPageContentStream(invoicePdf, page);
            cs.beginText();
            cs.setFont(PDType1Font.TIMES_ROMAN, 20);
            cs.newLineAtOffset(140, 750);
            cs.showText(ApplicationConstants.INVOICE_TITLE);
            cs.endText();

            cs.beginText();
            cs.setFont(PDType1Font.TIMES_ROMAN, 18);
            cs.newLineAtOffset(270, 690);
            cs.showText(ApplicationConstants.INVOICE_SUB_TITLE);
            cs.endText();

            Advertiser advertiser = invoiceDetail.getAdvertiser();
            cs.beginText();
            cs.setFont(PDType1Font.TIMES_ROMAN, 14);
            cs.setLeading(20f);
            cs.newLineAtOffset(60, 610);
            cs.showText("To : ");
            cs.newLine();
            cs.showText("Address : ");
            cs.newLine();
            cs.showText("");
            cs.newLine();
            cs.showText("Mobile : ");
            cs.newLine();
            cs.showText("Phone : ");
            cs.endText();

            cs.beginText();
            cs.setFont(PDType1Font.TIMES_ROMAN, 14);
            cs.setLeading(20f);
            cs.newLineAtOffset(170, 610);
            cs.showText(advertiser.getName());
            cs.newLine();
            cs.showText(advertiser.getAddress1()+" "+advertiser.getAddress2());
            cs.newLine();
            cs.showText(advertiser.getCity()+" "+advertiser.getState()+" "+advertiser.getCountry()+" - "+advertiser.getPin());
            cs.newLine();
            cs.showText(advertiser.getMobile());
            cs.newLine();
            cs.showText(advertiser.getPhone());
            cs.endText();

            cs.beginText();
            cs.setFont(PDType1Font.TIMES_ROMAN, 14);
            cs.newLineAtOffset(80, 540);
            cs.showText("Product Name");
            cs.endText();

            cs.beginText();
            cs.setFont(PDType1Font.TIMES_ROMAN, 14);
            cs.newLineAtOffset(200, 540);
            cs.showText("Unit Price");
            cs.endText();

            cs.beginText();
            cs.setFont(PDType1Font.TIMES_ROMAN, 14);
            cs.newLineAtOffset(310, 540);
            cs.showText("Quantity");
            cs.endText();

            cs.beginText();
            cs.setFont(PDType1Font.TIMES_ROMAN, 14);
            cs.newLineAtOffset(410, 540);
            cs.showText("Price");
            cs.endText();

            cs.beginText();
            cs.setFont(PDType1Font.TIMES_ROMAN, 12);
            cs.setLeading(20f);
            cs.newLineAtOffset(80, 520);
            for(InvoiceProductDetail i : invoiceDetail.getProductDetails()){
                cs.showText(i.getProduct().getName());
                cs.newLine();
            }
            cs.endText();

            cs.beginText();
            cs.setFont(PDType1Font.TIMES_ROMAN, 12);
            cs.setLeading(20f);
            cs.newLineAtOffset(200, 520);
            for(InvoiceProductDetail i : invoiceDetail.getProductDetails()){
                cs.showText(i.getRate()+"");
                cs.newLine();
            }
            cs.endText();

            cs.beginText();
            cs.setFont(PDType1Font.TIMES_ROMAN, 12);
            cs.setLeading(20f);
            cs.newLineAtOffset(310, 520);
            for(InvoiceProductDetail i : invoiceDetail.getProductDetails()){
                cs.showText(i.getQuantity()+"");
                cs.newLine();
            }
            cs.endText();

            cs.beginText();
            cs.setFont(PDType1Font.TIMES_ROMAN, 12);
            cs.setLeading(20f);
            cs.newLineAtOffset(410, 520);
            for(InvoiceProductDetail i : invoiceDetail.getProductDetails()){
                cs.showText(i.getPrice()+"");
                cs.newLine();
            }
            cs.endText();

            cs.beginText();
            cs.setFont(PDType1Font.TIMES_ROMAN, 14);
            cs.newLineAtOffset(310, (500-(20*invoiceDetail.getProductDetails().size())));
            cs.showText("Total: ");
            cs.endText();

            cs.beginText();
            cs.setFont(PDType1Font.TIMES_ROMAN, 14);
            //Calculating where total is to be written using number of products
            cs.newLineAtOffset(410, (500-(20*invoiceDetail.getProductDetails().size())));
            cs.showText(invoiceDetail.getInvoiceAmount()+"");
            cs.endText();

            //Close the content stream
            cs.close();
            invoicePdf.save(localPath+"invoice.pdf");
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }
}
